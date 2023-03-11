package com.mageddo.webhttpvanilla.java18plus;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

import java.net.InetSocketAddress;
import java.nio.file.Path;

public class SimpleFileServerWithCustomApisMain {
  public static void main(String[] args) throws Exception {
    final var fileHandler = SimpleFileServer.createFileHandler(Path.of("/tmp"));
    final var server = HttpServer.create(new InetSocketAddress(8080), 0);
    server.createContext("/static", fileHandler);
    server.createContext("/hello-world", exchange -> {
      final var response = "<h1>Hello World!</h1>";
      exchange.sendResponseHeaders(200, response.length());
      final var out = exchange.getResponseBody();
      out.write(response.getBytes());
    });
    server.start();
  }
}
