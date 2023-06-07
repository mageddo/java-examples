package com.mageddo.httpclient.apachehttpclient;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.mageddo.commons.concurrent.ThreadPool;
import com.mageddo.commons.concurrent.Threads;

import org.apache.commons.lang3.ThreadUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * When doing req 1 the thread in the pool can't do req 2.
 */
@Slf4j
public class SyncMainExample {
  @SneakyThrows
  public static void main(String[] args) {

    log.info("Starting");

    PoolingHttpClientConnectionManager pool = HttpClientUtils.createPool();

    dumpStats(pool);

    final var client = HttpClientUtils.build(pool);
    int tasksPoolSize = 300;
    final var executorService = (ThreadPoolExecutor) ThreadPool.newFixed(tasksPoolSize);
    final var stopWatch = StopWatch.createStarted();

    for (int i = 0; i < 100; i++) {
      executorService.submit(() -> {
        String name = Thread.currentThread().getName();
//        log.info("status=requesting, thread={}", name);

        reqSleep(client, name);
      });
    }

    Threads.sleep(1000);
    dumpStats(pool);


    executorService.shutdown();
    executorService.awaitTermination(10, TimeUnit.SECONDS);

    log.info("status=completed, time={}", stopWatch.getTime());


  }

  private static void dumpStats(PoolingHttpClientConnectionManager pool) {
    final var poolStats = pool.getTotalStats();
    log.info(
        "status=jvmStats {} httpClientAvailable={}, httpClientLeased={}, httpClientMax={}",
        JvmStatsUtils.dumpStats(), poolStats.getAvailable(), poolStats.getLeased(),
        poolStats.getMax()
    );
  }

  public static List<String> findThreadNames() {
    return ThreadUtils
        .getAllThreads()
        .stream()
        .map(Thread::getName)
        .collect(Collectors.toList())
        ;
  }

  private static void reqSleep(HttpClient client, String reqId) {
    final var stopWatch = StopWatch.createStarted();
    try {
      final var res = client.execute(
          new HttpGet("http://localhost:7272/sleep?time=3000"),
          new BasicHttpClientResponseHandler()
      );
      log.info("id={} time={} status=success, res={}", reqId, stopWatch.getTime(), res);
    } catch (Exception e) {
      log.error(
          "id={} time={} status=failed, res={}",
          reqId, stopWatch.getTime(), e.getMessage()
      );
    }
  }
}
