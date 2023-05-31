package com.mageddo.httpclient.apachehttpclient;

import java.net.URI;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.mageddo.commons.concurrent.ThreadPool;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestProducer;
import org.apache.hc.client5.http.async.methods.SimpleResponseConsumer;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Even with async http client When doing req 1 the thread in the pool can't do req 2.
 */
@Slf4j
public class AsyncMainExample {
  @SneakyThrows
  public static void main(String[] args) {

    final var server = new CheckoutControllerServer();
    try {
      log.info("Starting");
      server.start();
      Thread.sleep(1000);

      final var pool = new PoolingAsyncClientConnectionManager();
      pool.setMaxTotal(1);
      pool.setDefaultMaxPerRoute(1);

      final var ioReactorConfig = IOReactorConfig.custom()
          .setSoTimeout(Timeout.ofSeconds(5))
          .build();

      final var client = HttpAsyncClients.custom()
          .setConnectionManager(pool)
          .setIOReactorConfig(ioReactorConfig)
          .build();
      client.start();

      final var executorService = ThreadPool.newFixed(10);

      final var stopWatch = StopWatch.createStarted();
      reqSleep(client, "req 0");
      executorService.submit(() -> reqSleep(client, "req 1"));
      executorService.submit(() -> reqSleep(client, "req 2"));
      executorService.submit(() -> reqSleep(client, "req 3"));

      executorService.shutdown();
      executorService.awaitTermination(10, TimeUnit.SECONDS);

      client.close(CloseMode.GRACEFUL);
      log.info("status=completed, time={}", stopWatch.getTime());

    } finally {
      server.stop();
    }

  }

  private static void reqSleep(CloseableHttpAsyncClient client, String reqId) {
    final var stopWatch = StopWatch.createStarted();
    try {
      final Future<SimpleHttpResponse> future = client.execute(
          SimpleRequestProducer.create(new SimpleHttpRequest(
              "GET", URI.create("http://localhost:7272/sleep")
          )),
          SimpleResponseConsumer.create(),
          new FutureCallback<>() {
            @Override
            public void completed(final SimpleHttpResponse response) {
//              System.out.println("->" + new StatusLine(response));
//              System.out.println(response.getBody());
              log.info("{} completed!", reqId);
            }

            @Override
            public void failed(final Exception ex) {
//              System.out.println("->" + ex);
            }

            @Override
            public void cancelled() {
//              System.out.println(" cancelled");
            }

          });
      final var res = future.get();
      log.info("id={} time={} status=success, res={}", reqId, stopWatch.getTime(),
          res.getBodyText());
    } catch (Exception e) {
      log.error(
          "id={} time={} status=failed, res={}",
          reqId, stopWatch.getTime(), e.getMessage(), e
      );
    }
  }
}
