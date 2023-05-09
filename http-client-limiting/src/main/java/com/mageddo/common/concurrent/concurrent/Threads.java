package com.mageddo.common.concurrent.concurrent;

import java.time.Duration;

import lombok.SneakyThrows;

public class Threads {
  public static void sleep(Duration duration) {
    sleep(duration.toMillis());
  }

  @SneakyThrows
  public static void sleep(long millis) {
    Thread.sleep(millis);
  }

  public static Thread createDaemonThread(Runnable r) {
    final Thread t = new Thread(r);
    t.setDaemon(true);
    return t;
  }
}
