package com.mageddo.common;

import java.time.Duration;

public class Threads {
  private Threads() {
  }

  public static void sleep(Duration d) {
    try {
      Thread.sleep(d);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
