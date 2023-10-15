package com.mageddo.commons;

public class Threads {
  public static void sleep(long millis){
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
