package com.mageddo.httpclient.java11;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.mageddo.commons.concurrent.ThreadPool;
import com.mageddo.commons.concurrent.Threads;
import com.mageddo.httpclient.CheckoutControllerServer;

import org.apache.commons.lang3.ThreadUtils;
import org.apache.commons.lang3.time.StopWatch;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Java 11 http client will create threads in the background that's why it can process in parallel
 */
@Slf4j
public class Java11Main {
  @SneakyThrows
  public static void main(String[] args) {

    final var server = new CheckoutControllerServer();
    try {
      log.info("Starting");
      server.start();
      Thread.sleep(1000);

      final var client = HttpClient
          .newBuilder()
          .connectTimeout(Duration.ofSeconds(2))
          .build();

      final var executorService = ThreadPool.newFixed(10);

      final var stopWatch = StopWatch.createStarted();

      reqSleep(client, "req 0");
      executorService.submit(() -> reqSleep(client, "req 1"));
      executorService.submit(() -> reqSleep(client, "req 2"));

      Threads.sleep(100);
      final List<String> threads =
          ThreadUtils.getAllThreads().stream().map(Thread::getName).collect(Collectors.toList());
      log.info("all threads={}", threads);

      executorService.shutdown();
      executorService.awaitTermination(10, TimeUnit.SECONDS);

      log.info("status=completed, time={}", stopWatch.getTime());

    } finally {
      server.stop();
    }
  }

  @SneakyThrows
  private static void reqSleep(HttpClient client, String reqId) {
    final var stopWatch = StopWatch.createStarted();
    try {
//      final var publisher = HttpRequest.BodyPublishers.ofString("Abc");
      final var uri = URI.create("http://localhost:7272/sleep");
      final var res = client
          .send(
              HttpRequest
                  .newBuilder()
                  .GET()
//                  .POST(publisher)
                  .header("User-Agent", "MonitoringOpenApi")
                  .uri(uri)
                  .timeout(Duration.ofSeconds(4))
                  .build(),
              responseInfo -> HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8)
          );

      log.info("id={} time={} status=success, res={}", reqId, stopWatch.getTime(),
          res.body());
    } catch (Exception e) {
      log.error(
          "id={} time={} status=failed, res={}",
          reqId, stopWatch.getTime(), e.getMessage(), e
      );
    }
  }
}
