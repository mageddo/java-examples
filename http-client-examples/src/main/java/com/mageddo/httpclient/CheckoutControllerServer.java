package com.mageddo.httpclient;

import com.mageddo.commons.concurrent.Threads;
import com.sun.net.httpserver.HttpExchange;

import org.apache.hc.core5.net.URIBuilder;

import lombok.SneakyThrows;
import spark.Spark;

public class CheckoutControllerServer {

  @SneakyThrows
  public void start() {

    Spark.port(7272);
    Spark.threadPool(305);
    Spark.get("/sleep", (req, res) -> {
      final int time = Integer.parseInt(req.queryParamOrDefault("time", "500"));
      Threads.sleep(time);
      return "Slept for " + time + " millis\n";
    });

//    this.server = HttpServer.create(new InetSocketAddress(7272), 0);
//    this.server.createContext("/sleep", exchange -> {
//      final int time = getSleepTime(exchange);
//      try {
//        Thread.sleep(time);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
//      final var data = ("Sleeped for " + time + " millis\n").getBytes(StandardCharsets.UTF_8);
//      exchange.sendResponseHeaders(200, data.length);
//      exchange
//          .getResponseBody()
//          .write(data)
//      ;
//    });
//    server.start();
  }

  private int getSleepTime(HttpExchange exchange) {
    final var uriBuilder = new URIBuilder(exchange.getRequestURI());
    if (uriBuilder.getQueryParams().isEmpty()) {
      return 500;
    }
    final var timeParam = uriBuilder.getFirstQueryParam("time");
    final var time = timeParam != null ? Integer.parseInt(timeParam.getValue()) : 500;
    return time;
  }

  public void stop() {
    Spark.stop();
    Spark.awaitStop();
  }
}
