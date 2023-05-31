package com.mageddo.httpclient.apachehttpclient;

import java.util.concurrent.TimeUnit;

import com.mageddo.commons.concurrent.ThreadPool;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * When doing req 1 the thread in the pool can't do req 2.
 */
@Slf4j
public class SyncMainExample {
  @SneakyThrows
  public static void main(String[] args) {

    final var server = new CheckoutControllerServer();
    try {
      log.info("Starting");
      server.start();
      Thread.sleep(1000);

      final var client = OkHttpClientUtils.build();
      final var executorService = ThreadPool.newFixed(10);

      final var stopWatch = StopWatch.createStarted();

      reqSleep(client, "req 0");
      executorService.submit(() -> reqSleep(client, "req 1"));
      executorService.submit(() -> reqSleep(client, "req 2"));

      executorService.shutdown();
      executorService.awaitTermination(10, TimeUnit.SECONDS);

      log.info("status=completed, time={}", stopWatch.getTime());

    } finally {
      server.stop();
    }

  }

  private static void reqSleep(HttpClient client, String reqId) {
    final var stopWatch = StopWatch.createStarted();
    try {
      final var res = client.execute(
          new HttpGet("http://localhost:7272/sleep"),
          new BasicHttpClientResponseHandler()
      );
      log.info("id={} time={} status=success, res={}", reqId, stopWatch.getTime(), res);
    } catch (Exception e){
      log.error(
          "id={} time={} status=failed, res={}",
          reqId, stopWatch.getTime(), e.getMessage(), e
      );
    }
  }
}
