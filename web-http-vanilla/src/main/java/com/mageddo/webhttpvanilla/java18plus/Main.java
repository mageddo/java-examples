package com.mageddo.webhttpvanilla.java18plus;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;
import com.sun.net.httpserver.SimpleFileServer.OutputLevel;

import java.net.InetSocketAddress;
import java.nio.file.Path;

public class Main {
  public static void main(String[] args) {
    final HttpServer server = SimpleFileServer.createFileServer(
      new InetSocketAddress(8080),
      Path.of("/tmp"),
      OutputLevel.VERBOSE
    );
    server.start();
  }
}
