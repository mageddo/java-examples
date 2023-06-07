package com.mageddo.resteasy;

import com.mageddo.resteasy.testing.InMemoryRestServer;

import org.junit.ClassRule;
import org.junit.Test;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @see
 * <a href="https://stackoverflow.com/questions/31954353/how-can-i-upload-image-and-data-in-the-same-request-with-angular-and-resteasy">How can I upload image and data in the same request with angular and resteasy?</a>
 */
public class RequestSpecificTimeoutTest {

  @ClassRule
  public static final InMemoryRestServer server = new InMemoryRestServer(Proxy.class);

  @Test
  public void mustFailDueTimeout() {

    // arrange


    // act
    try {
      RestEasy.newClient(1)
          .target(server.getURL())
          .path("/slow-endpoint")
          .request()
          .property(RestEasy.SOCKET_TIMEOUT, 200)
          .property(RestEasy.CONNECT_TIMEOUT, 100)
          .property(RestEasy.CONNECTION_REQUEST_TIMEOUT, 200)
          .get(String.class);
      fail("Must fail due timeout");
    } catch (Exception e) {
      assertTrue(
          e.getMessage(),
          e.getMessage().contains("RESTEASY004655: Unable to invoke request")
      );
    }

  }

  @Test
  public void mustGetWithSuccessBecauseSleepIsLessThanTimeout() {

    // arrange


    // act
    final String res = RestEasy.newClient(1)
        .target(server.getURL())
        .path("/slow-endpoint")
        .request()
        .property(RestEasy.SOCKET_TIMEOUT, 800)
        .property(RestEasy.CONNECT_TIMEOUT, 50)
        .property(RestEasy.CONNECTION_REQUEST_TIMEOUT, 20)
        .get(String.class);

    // assert
    assertEquals(":)", res);

  }


  @Path("/")
  public static class Proxy {
    @GET
    @Path("/slow-endpoint")
    public Response get() throws InterruptedException {
      Thread.sleep(500);
      return Response.ok(":)").build();
    }
  }

}
