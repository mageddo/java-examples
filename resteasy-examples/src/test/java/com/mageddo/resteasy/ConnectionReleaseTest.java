package com.mageddo.resteasy;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mageddo.resteasy.testing.InMemoryRestServer;

import org.apache.commons.io.IOUtils;
import org.apache.http.pool.PoolStats;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConnectionReleaseTest {

  @ClassRule
  public static final InMemoryRestServer server =
      new InMemoryRestServer(ConnectionReleaseTest.Proxy.class);

  WebTarget webTarget;
  RestEasyClient client;

  @Before
  public void before() {
    this.client = RestEasy.newRestEasyClient(1);
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
    } catch (WebApplicationException e){
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
    } catch (ProcessingException e){
      assertEquals(0, getPoolStats().getLeased());
      assertEquals(0, getPoolStats().getAvailable());
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
      try {
        Thread.sleep(10_000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return Response.ok("Hello World!!!").build();
    }


  }
}
