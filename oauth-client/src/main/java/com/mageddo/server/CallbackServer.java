package com.mageddo.server;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class CallbackServer implements AutoCloseable {

  public static final int PORT = 9898;

  private final int port;
  private final HttpServer server;

  public CallbackServer(int port) {
    this.port = port;
    this.server = this.createServer();
  }

  public void start() {
    server.createContext("/oauth/callback", new OAuthCallbackHandler());
    // Java 21 – pode trocar por virtual threads se quiser
    server.setExecutor(Executors.newCachedThreadPool());
    server.start();
  }

  public static CallbackServer create() {
    final var server = new CallbackServer(PORT);
    server.start();
    return server;
  }

  private HttpServer createServer() {
    try {
      return HttpServer.create(new InetSocketAddress(this.port), 0);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public void close() {
    this.server.stop(0);
  }
}
