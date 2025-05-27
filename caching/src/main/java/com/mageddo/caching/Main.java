package com.mageddo.caching;

import java.time.LocalDateTime;
import java.util.UUID;

public class Main {


  public static void main(String[] args) {
    final var cache = new Cache<String, Object>();

    for (int i = 0; i < 100_000; i++) {

      putItemOnCache(i, cache);

      if (i % 20_000 == 0) {
        System.out.printf("memory=%.2f%n", findUsedMemoryKBytes());
      }
    }

  }

  private static void putItemOnCache(int i, Cache<String, Object> cache) {
    final var uuid = UUID.randomUUID();
    final var v = String.format("%s-%s-%s", uuid, i, LocalDateTime.now());
    cache.put(uuid.toString(), v);
  }

  public static double findUsedMemoryKBytes() {
    return findUsedMemoryBytes() / 1024.0;
  }

  public static long findUsedMemoryBytes() {
    final var runtime = Runtime.getRuntime();
    return runtime.totalMemory() - runtime.freeMemory();
  }

}


