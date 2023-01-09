import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * $ javac SpringLogLevelChanger.java
 * $ java SpringLogLevelChanger "http://localhost:8080/actuator/loggers/com.acme"
 * You are done!
 */
public class SpringLogLevelChanger {

  static {
    HttpsTrustManager.allowAllSSL();
  }

  public static void main(String[] args) throws Exception {

    while (true) {
      changeLogLevel(new URL(args[0]));
      Thread.sleep(1000 / 30);
    }

  }

  public static void changeLogLevel(URL url) throws IOException {

    final HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setConnectTimeout(100_000);
    con.setReadTimeout(100_000);
    con.setInstanceFollowRedirects(true);
    con.setDoOutput(true);

    con.setRequestProperty("Accept-Charset", "utf-8");
    con.setRequestProperty("Content-Type", "application/json");

    try (final PrintWriter out = new PrintWriter(con.getOutputStream())) {
      out.print("{\"configuredLevel\": \"TRACE\"}");
    }

    System.out.printf("code: %d%n%n", con.getResponseCode());
    System.out.println("Error Stream");
    System.out.println("---------------------");
    System.out.println(readToString(con.getErrorStream()));
    System.out.println();
    System.out.println("Response");
    System.out.println("---------------------");
    System.out.println(readToString(con.getInputStream()));

  }

  public static String readToString(InputStream in) throws IOException {
    if (in == null) {
      return "";
    }
    int bufferSize = 1024;
    char[] buffer = new char[bufferSize];
    StringBuilder out = new StringBuilder();
    Reader bin = new InputStreamReader(in, StandardCharsets.UTF_8);
    for (int numRead; (numRead = bin.read(buffer, 0, buffer.length)) > 0; ) {
      out.append(buffer, 0, numRead);
    }
    return out.toString();
  }

  public static class HttpsTrustManager implements X509TrustManager {

    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};
    private static TrustManager[] trustManagers;

    public static void allowAllSSL() {
      HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

        @Override
        public boolean verify(String arg0, SSLSession arg1) {
          return true;
        }

      });

      SSLContext context = null;
      if (trustManagers == null) {
        trustManagers = new TrustManager[]{new HttpsTrustManager()};
      }

      try {
        context = SSLContext.getInstance("TLS");
        context.init(null, trustManagers, new SecureRandom());
      } catch (NoSuchAlgorithmException | KeyManagementException e) {
        e.printStackTrace();
      }

      HttpsURLConnection.setDefaultSSLSocketFactory(context != null ? context.getSocketFactory()
          : null);
    }

    @Override
    public void checkClientTrusted(
        X509Certificate[] x509Certificates, String s)
        throws java.security.cert.CertificateException {

    }

    @Override
    public void checkServerTrusted(
        X509Certificate[] x509Certificates, String s)
        throws java.security.cert.CertificateException {

    }

    public boolean isClientTrusted(X509Certificate[] chain) {
      return true;
    }

    public boolean isServerTrusted(X509Certificate[] chain) {
      return true;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return _AcceptedIssuers;
    }
  }

}
