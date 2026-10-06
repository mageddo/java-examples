import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import jakarta.ws.rs.client.Client;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;
import org.jboss.resteasy.client.jaxrs.internal.ClientInvocation;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;

public class IdleProbe {
  static String base;
  static String proxyBase;
  static SSLContext ssl;
  static String protocol;
  static long idleMillis;
  static int readMillis;

  public static void main(String[] args) throws Exception {
    base = "https://localhost:" + Files.readString(Path.of("port.txt"));
    proxyBase = "https://localhost:" + Files.readString(Path.of("proxy_port.txt"));
    protocol = args[0];
    idleMillis = Long.parseLong(args[1]);
    readMillis = Integer.parseInt(args[2]);
    ssl = trustLocalCertificate();
    System.out.printf("java=%s TLS=%s idle_ms=%d read_ms=%d pool=5 validateAfterInactivity=2000ms%n",
        System.getProperty("java.version"), protocol, idleMillis, readMillis);
    if (args.length > 3) {
      compare(args[3], "reuse", 0);
      return;
    }
    compare("healthy", "reuse", 0);
    compare("close", "reuse", 0);
    compare("silent", "reuse", 0);
    compare("silent", "new", 0);
    compare("silent", "ttl", 1000);
    compare("silent", "evict", 0);
    compare("blackhole", "reuse", 0);
    compare("blackhole", "new", 0);
    compare("blackhole", "ttl", 1000);
    compare("blackhole", "evict", 0);
  }

  static SSLContext trustLocalCertificate() throws Exception {
    final var store = KeyStore.getInstance(KeyStore.getDefaultType());
    store.load(null);
    try (final var input = Files.newInputStream(Path.of("cert.pem"))) {
      store.setCertificateEntry("local", CertificateFactory.getInstance("X.509").generateCertificate(input));
    }
    return SSLContexts.custom().loadTrustMaterial(store, null).build();
  }

  static Client create(long ttl) {
    final var config = RequestConfig.custom().setConnectionRequestTimeout(3000)
        .setConnectTimeout(500).setSocketTimeout(readMillis).setRedirectsEnabled(true).build();
    final var builder = HttpClientBuilder.create().setDefaultRequestConfig(config)
        .setMaxConnTotal(5).setMaxConnPerRoute(5)
        .setSSLSocketFactory(new SSLConnectionSocketFactory(ssl, new String[]{protocol}, null,
            SSLConnectionSocketFactory.getDefaultHostnameVerifier()));
    if (ttl > 0) {
      builder.setConnectionTimeToLive(ttl, TimeUnit.MILLISECONDS);
    }
    final var engine = new ApacheHttpClient43Engine(builder.build(), true) {
      @Override
      protected void loadHttpMethod(ClientInvocation request, HttpRequestBase method) throws Exception {
        super.loadHttpMethod(request, method);
        if (method.getConfig() == null) {
          throw new IllegalStateException("RESTEasy did not preserve default request config");
        }
        final var requestConfig = RequestConfig.copy(method.getConfig()).setRedirectsEnabled(true).build();
        if (requestConfig.getSocketTimeout() != readMillis) {
          throw new IllegalStateException("Read timeout lost: " + requestConfig.getSocketTimeout());
        }
        method.setConfig(requestConfig);
      }
    };
    return new ResteasyClientBuilderImpl().httpEngine(engine).build();
  }

  static void compare(String path, String strategy, long ttl) throws Exception {
    var client = create(ttl);
    try {
      request(client, path, strategy, "first");
      Thread.sleep(idleMillis);
      if (strategy.equals("new")) {
        client.close();
        client = create(ttl);
      }
      if (strategy.equals("evict")) {
        final var engine = (ApacheHttpClient43Engine) ((org.jboss.resteasy.client.jaxrs.ResteasyClient) client).httpEngine();
        engine.getHttpClient().getConnectionManager().closeIdleConnections(1, TimeUnit.SECONDS);
      }
      request(client, path, strategy, "after_idle");
    } finally {
      client.close();
    }
  }

  static void request(Client client, String path, String strategy, String stage) {
    final var start = System.nanoTime();
    var endpoint = base;
    if (path.equals("blackhole")) {
      endpoint = proxyBase;
    }
    try (final var response = client.target(endpoint + "/" + path).request().get()) {
      final var body = response.readEntity(String.class).strip();
      System.out.printf("scenario=%s strategy=%s stage=%s elapsed_ms=%.1f status=%d %s%n",
          path, strategy, stage, (System.nanoTime() - start) / 1e6, response.getStatus(), body);
    } catch (Exception error) {
      var cause = (Throwable) error;
      while (cause.getCause() != null) {
        cause = cause.getCause();
      }
      System.out.printf("scenario=%s strategy=%s stage=%s elapsed_ms=%.1f error=%s message=%s%n",
          path, strategy, stage, (System.nanoTime() - start) / 1e6, cause.getClass().getSimpleName(), cause.getMessage());
    }
  }
}
