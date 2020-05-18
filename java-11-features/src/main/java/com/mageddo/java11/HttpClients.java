package com.mageddo.java11;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class HttpClients {
  public String get(String url) throws IOException, InterruptedException {
    final var res = HttpClient
        .newHttpClient()
        .send(
            HttpRequest
                .newBuilder()
                .GET()
                .header("User-Agent", "Java 11")
                .uri(URI.create(url))
                .build(),
            responseInfo -> {
              System.out.printf("status = %d%n", responseInfo.statusCode());
              return HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8);
            }
        );
    return res.body();
  }
}
