package com.mageddo.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class OAuthCallbackHandler implements HttpHandler {

  @Override
  public void handle(final HttpExchange exchange) throws IOException {

    if (!"GET".equals(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(405, -1);
      return;
    }

    final var uri = exchange.getRequestURI();
    final var queryParams = parseQueryParams(uri);

    final var code = queryParams.get("code");

    final var response =
        "code=" + code + "\n\nraw=" + uri.toString() + "\n";

    final var bytes = response.getBytes(StandardCharsets.UTF_8);

    exchange.getResponseHeaders()
        .add("Content-Type", "application/json; charset=utf-8");

    exchange.sendResponseHeaders(200, bytes.length);

    try (final OutputStream os = exchange.getResponseBody()) {
      os.write(bytes);
    }
  }

  private Map<String, String> parseQueryParams(final URI uri) {
    if (uri.getRawQuery() == null) {
      return Map.of();
    }

    return Arrays.stream(uri.getRawQuery().split("&"))
        .map(p -> p.split("=", 2))
        .collect(Collectors.toMap(
            p -> decode(p[0]),
            p -> p.length > 1 ? decode(p[1]) : ""
        ));
  }

  private String decode(final String value) {
    return URLDecoder.decode(value, StandardCharsets.UTF_8);
  }
}
