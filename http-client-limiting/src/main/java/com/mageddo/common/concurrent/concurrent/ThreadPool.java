package com.mageddo.common.concurrent.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
}
