package com.mageddo.common.concurrent.concurrent;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.mageddo.commons.lang.Singletons;
import com.mageddo.commons.lang.exception.UnchekedInterruptedException;

public class ThreadPool {

  public static final int DEFAULT_SIZE = 5;

  public static ScheduledExecutorService newScheduled(int coreSize) {
    return Executors.newScheduledThreadPool(
        coreSize,
        Threads::createDaemonThread
    );
  }

  public static ExecutorService newFixed(int maxSize) {
    return Executors.newFixedThreadPool(maxSize, Threads::createDaemonThread);
  }

  public static ExecutorService main() {
    return main(DEFAULT_SIZE);
  }

  /**
   * Will create a singleton pool with the specified size, the size specified on the JVM first
   * call will be considered to create  the pool.
   */
  public static ExecutorService main(final int maxSize) {
    return Singletons.createOrGet(
        "ThreadPool-fixed",
        () -> newFixed(maxSize)
    );
  }

  public static ScheduledExecutorService scheduled() {
    return scheduled(DEFAULT_SIZE);
  }

  /**
   * Will create a singleton pool with the specified size, the size specified on the JVM first
   * call will be considered to create  the pool.
   */
  public static ScheduledExecutorService scheduled(final int coreSize) {
    return Singletons.createOrGet(
        "ThreadPool-schecheduled",
        () -> newScheduled(coreSize)
    );
  }

  public static void terminateAndWait(ExecutorService pool, Duration duration) {
    try {
      pool.shutdown();
      pool.awaitTermination(duration.toMillis(), TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      throw new UnchekedInterruptedException(e);
    }
  }
}
