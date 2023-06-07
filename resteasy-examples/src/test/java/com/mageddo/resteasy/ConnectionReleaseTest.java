package com.mageddo.resteasy;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mageddo.commons.concurrent.ThreadPool;
import com.mageddo.commons.concurrent.Threads;
import com.mageddo.httpclient.AutoExpiryPoolingHttpClientConnectionManager;
import com.mageddo.httpclient.ConnectionTerminatorInterceptor;
import com.mageddo.resteasy.testing.InMemoryRestServer;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertEquals;

@Slf4j
public class ConnectionReleaseTest {

  @ClassRule
  public static final InMemoryRestServer server =
      new InMemoryRestServer(ConnectionReleaseTest.Proxy.class);

  WebTarget webTarget;
  RestEasyClient client;

  @Before
  public void before() {
    this.client = RestEasy.newRestEasyClient(10);
    this.webTarget = this.client
        .getClient()
        .target(server.getURL())
    ;
  }

  @Test
  public void mustAutomaticallyCloseAndReleaseResourceWhenConsumingString() {
    // arrange

    // act

    assertEquals(0, getPoolStats().getLeased());
    assertEquals(0, getPoolStats().getAvailable());

    final var res = this.webTarget
        .path("/hello-world")
        .request()
        .get(String.class);

    // assert
    assertEquals("Hello World!!!", res);
    assertEquals(0, getPoolStats().getLeased());
    assertEquals(1, getPoolStats().getAvailable());
  }

  @Test
  public void mustNOTAutomaticallyCloseAndReleaseResourceWhenConsumingInputStream() throws IOException {
    // arrange

    // act
    // assert

    assertEquals(0, getPoolStats().getLeased());
    assertEquals(0, getPoolStats().getAvailable());

    final var res = this.webTarget
        .path("/hello-world")
        .request()
        .get(InputStream.class);

    assertEquals(1, getPoolStats().getLeased());
    assertEquals(0, getPoolStats().getAvailable());

    final var msg = IOUtils.toString(res, StandardCharsets.UTF_8);
    res.close();

    assertEquals("Hello World!!!", msg);
    assertEquals(0, getPoolStats().getLeased());
    assertEquals(1, getPoolStats().getAvailable());

  }

  @Test
  public void mustNOTAutomaticallyCloseAndReleaseResourceWhenGetResponseObject() throws IOException {
    // arrange

    // act
    // assert

    assertEquals(0, getPoolStats().getLeased());
    assertEquals(0, getPoolStats().getAvailable());

    final var res = this.webTarget
        .path("/hello-world")
        .request()
        .get();

    assertEquals(1, getPoolStats().getLeased());
    assertEquals(0, getPoolStats().getAvailable());

    res.close();
    assertEquals(0, getPoolStats().getLeased());
    assertEquals(1, getPoolStats().getAvailable());

  }


  @Test
  public void mustAutomaticallyCloseWhenGetStatusError() throws IOException {
    // arrange

    // act
    // assert
    assertEquals(0, getPoolStats().getLeased());
    assertEquals(0, getPoolStats().getAvailable());

    try {
      this.webTarget
          .path("/internal-server-error")
          .request()
          .get(String.class);
      Assert.fail();
    } catch (WebApplicationException e) {
      assertEquals(0, getPoolStats().getLeased());
      assertEquals(1, getPoolStats().getAvailable());
    }

  }

  @Test
  public void mustAutomaticallyCloseWhenGetTimeoutError() throws IOException {
    // arrange

    // act
    // assert
    assertEquals(0, getPoolStats().getLeased());
    assertEquals(0, getPoolStats().getAvailable());

    try {
      this.webTarget
          .path("/timeout-error")
          .property(RestEasy.SOCKET_TIMEOUT, 300)
          .request()
          .get(String.class);
      Assert.fail();
    } catch (ProcessingException e) {
      assertEquals(0, getPoolStats().getLeased());
      assertEquals(0, getPoolStats().getAvailable());
    }

  }

  @Test
  public void mustCloseAllAvailableConnectionsAfterTTL() throws Exception {

    // arrange
    final var threadPoolSize = 5;
    final var executorService = ThreadPool.newFixed(threadPoolSize);
    final var pool = new PoolingHttpClientConnectionManager(
        getDefaultRegistry(),
        null, null, null,
        1, TimeUnit.SECONDS
    );
    pool.setMaxTotal(100);
    pool.setDefaultMaxPerRoute(100);
    final var localClient = RestEasy.newRestEasyClient(pool).getClient();


    assertEquals(0, pool.getTotalStats().getLeased());
    assertEquals(0, pool.getTotalStats().getAvailable());

    // act
    parallelReqs(threadPoolSize, executorService, localClient);
    executorService.shutdown();
    executorService.awaitTermination(1, TimeUnit.SECONDS);

    // assert
    assertEquals(0, pool.getTotalStats().getLeased());
    assertEquals(5, pool.getTotalStats().getAvailable());

    Thread.sleep(1100);
    localClient
        .target(server.getURL())
        .path("/sleep")
        .request()
        .get(String.class);

    assertEquals(0, pool.getTotalStats().getLeased());
    assertEquals(1, pool.getTotalStats().getAvailable());
  }

  @Test
  public void mustCloseAllConnectionsAfterTTLEvenWhenLeased() throws Exception {

    // arrange
    final var threadPoolSize = 5;
    final var executorService = ThreadPool.newFixed(threadPoolSize);
    final var pool = new AutoExpiryPoolingHttpClientConnectionManager(
        100, Duration.ofSeconds(1)
    );
    final var localClient = RestEasy.newRestEasyClient(pool).getClient();

    assertEquals(0, pool.getTotalStats().getLeased());
    assertEquals(0, pool.getTotalStats().getAvailable());

    // act
    for (int i = 0; i < threadPoolSize; i++) {
      executorService.submit(() -> {
        localClient
            .target(server.getURL())
            .path("/sleep")
            .request()
            .get(InputStream.class); // leaving leased <<<<<<
      });
    }

    executorService.shutdown();
    executorService.awaitTermination(1, TimeUnit.SECONDS);

    // assert
    assertEquals(5, pool.getTotalStats().getLeased());
    assertEquals(0, pool.getTotalStats().getAvailable());

    Thread.sleep(1100);
    pool.closeExpiredConnections(); // <<<<<<<
    localClient
        .target(server.getURL())
        .path("/sleep")
        .request()
        .get(String.class);

    assertEquals(0, pool.getTotalStats().getLeased());
    assertEquals(1, pool.getTotalStats().getAvailable());
  }


  @Test
  public void terminatorMustCloseUnclosedConnections() throws Exception {

    // arrange
    final var pool = new PoolingHttpClientConnectionManager();
    pool.setMaxTotal(100);
    pool.setDefaultMaxPerRoute(100);

    final var requestConfig = RequestConfig.custom()
        .setConnectionRequestTimeout(100)
        .setSocketTimeout(1000)
        .setConnectTimeout(500)
        .build();

    final HttpClient httpClient = HttpClientBuilder.create()
        .setConnectionManager(pool)
        .setDefaultRequestConfig(requestConfig)
        .addInterceptorLast(new ConnectionTerminatorInterceptor())
        .build();

    final var client = new ResteasyClientBuilder()
        .httpEngine(new ApacheHttpClient43Engine(httpClient, true))
        .build();


    assertEquals(0, pool.getTotalStats().getLeased());
    assertEquals(0, pool.getTotalStats().getAvailable());

    // act
    client
        .target(server.getURL())
        .path("/sleep")
        .request()
        .get(InputStream.class); // leaving leased <<<<<<

    assertEquals(1, pool.getTotalStats().getLeased());
    assertEquals(0, pool.getTotalStats().getAvailable());

    client
        .target(server.getURL())
        .path("/sleep")
        .request()
        .get(String.class);
    assertEquals(1, pool.getTotalStats().getLeased());
    assertEquals(1, pool.getTotalStats().getAvailable());

    Threads.sleep(1500);
    client
        .target(server.getURL())
        .path("/sleep")
        .request()
        .get(String.class);

    assertEquals(0, pool.getTotalStats().getLeased());
    assertEquals(2, pool.getTotalStats().getAvailable());
  }


  private static void parallelReqs(int threadPoolSize, ExecutorService executorService,
      Client localClient) {
    for (int i = 0; i < threadPoolSize; i++) {
      executorService.submit(() -> {
        localClient
            .target(server.getURL())
            .path("/sleep")
            .request()
            .get(String.class);
      });
    }
  }

  private PoolStats getPoolStats() {
    return this.client.getPool().getTotalStats();
  }

  @Path("/")
  public static class Proxy {

    @GET
    @Path("/hello-world")
    @Produces(MediaType.TEXT_PLAIN)
    public Response helloWorld() {
      return Response.ok("Hello World!!!").build();
    }

    @GET
    @Path("/internal-server-error")
    @Produces(MediaType.TEXT_PLAIN)
    public Response internalServerError() {
      return Response.serverError().entity("Failed!").build();
    }

    @GET
    @Path("/timeout-error")
    @Produces(MediaType.TEXT_PLAIN)
    public Response timeoutError() {
      Threads.sleep(10_000);
      return Response.ok("Hello World!!!").build();
    }

    @GET
    @Path("/sleep")
    @Produces(MediaType.TEXT_PLAIN)
    public Response sleep() {
      Threads.sleep(300);
      return Response.ok("Hello World!!!").build();
    }


  }

  static Registry<ConnectionSocketFactory> getDefaultRegistry() {
    return RegistryBuilder.<ConnectionSocketFactory>create()
        .register("http", PlainConnectionSocketFactory.getSocketFactory())
        .register("https", SSLConnectionSocketFactory.getSocketFactory())
        .build();
  }
}
