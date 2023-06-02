package com.mageddo.httpclient.apachehttpclient;

import com.mageddo.httpclient.CheckoutControllerServer;

public class HttpServerMain {
  public static void main(String[] args) throws InterruptedException {
    final var server = new CheckoutControllerServer();
    server.start();
    Thread.sleep(Integer.MAX_VALUE);
  }
}
