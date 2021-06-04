package com.mageddo.ratelimit;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.util.concurrent.RateLimiter;

public class RateLimitMain {

  public static void main(String[] args) throws InterruptedException {

    final var limiter = RateLimiter.create(10.0);

    Thread.sleep(100);

    final var map = new HashMap<String, AtomicInteger>();
    for (int i = 0; true; i++) {

      final var acquired = limiter.tryAcquire(1);
      final var t = String.format("%s", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
      final var acquiredKey = String.format("%s-acquired", t);
      final var notAcquiredKey = String.format("%s-not", t);

      if (acquired) {
        map.compute(acquiredKey, (k, v) -> {
          if (v == null) {
            return new AtomicInteger(1);
          }
          v.incrementAndGet();
          return v;
        });

        final var acquiredInThisSecond = String.valueOf(map.get(acquiredKey));
        final var notAcquiredInThisSecond = String.valueOf(map.get(notAcquiredKey));
        if (Objects.equals(acquiredInThisSecond, "10")) {
          System.out.printf(
              "acquired=%b, now=%s, acquiredAtThisSecond=%s, notAcquired=%s, %n",
              acquired, t, acquiredInThisSecond, notAcquiredInThisSecond
          );
        }
      } else {
        map.compute(notAcquiredKey, (k, v) -> {
          if (v == null) {
            return new AtomicInteger(1);
          }
          v.incrementAndGet();
          return v;
        });
      }
    }


  }
}
