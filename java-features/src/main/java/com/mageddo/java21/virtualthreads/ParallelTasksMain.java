package com.mageddo.java21.virtualthreads;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.StructuredTaskScope;

import com.mageddo.common.Threads;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParallelTasksMain {

  static void main() throws Exception {
    option1();
    option2();
  }

  private static void option2() {
    final var executor = Executors.newVirtualThreadPerTaskExecutor();
    try (executor) {

      executor.submit(() -> Threads.sleep(Duration.ofSeconds(3)));
      executor.submit(() -> Threads.sleep(Duration.ofSeconds(1)));

      log.info("submitted");
    }
    log.info("ended");
  }

  static void option1() throws Exception {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

      final var f1 = scope.fork(() -> LocalDateTime.now());
      final var f2 = scope.fork(() -> LocalDateTime.now());

      scope.join();
      scope.throwIfFailed();

      log.info("a={}, b={}", f1.get(), f2.get());
    }
  }
}
