package com.mageddo.java11;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpClientsTest {

  private final HttpClients httpClients = new HttpClients();

  /**
   * Test is not creating a mock web server for tests, but should
   */
  @Test
  void mustGetAcmeWebSite() throws IOException, InterruptedException {

    // arrange

    // act
    final var responseBody = this.httpClients.get("http://acme.com");

    // assert
    assertTrue(responseBody.contains("ACME Laboratories"));

  }

}
