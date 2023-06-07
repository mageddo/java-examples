package com.mageddo.java21.virtualthreads;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.time.StopWatch;

import lombok.extern.slf4j.Slf4j;

/**
 * Running 30k threads and looking at linux system monitor:
 *   * Native Threads: 15% CPU 1.1GB of RAM ran in 11800ms
 *   * Virtual Threads: 0% CPU 110MB RAM ran in 5200ms
 */
@Slf4j
public class VirtualThreadsVsPlatformMain {
  public static void main(String[] args) {
    log.info("pid={}", ProcessHandle.current().pid());
    final var stopWatch = StopWatch.createStarted();
    final var threads = 30_000;
    final var executor = Executors.newVirtualThreadPerTaskExecutor();
//    final var executor = Executors.newFixedThreadPool(threads);
    try (executor) {
      IntStream.range(0, threads).forEach(i -> {
        executor.submit(() -> {
          Thread.sleep(Duration.ofSeconds(5));
          return i;
        });
      });
    }
    log.info("time={}", stopWatch.getTime());
  }
}
