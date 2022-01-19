package com.mageddo.failsafe;

import java.io.UncheckedIOException;
import java.time.Duration;

import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;

public class CircuitBreakerMain {
  public static void main(String[] args) {
    final var breaker = new CircuitBreaker<String>()
//        .builder()
        .handle(UncheckedIOException.class)
        .withFailureThreshold(3, 10)
        .withSuccessThreshold(5)
        .withDelay(Duration.ofMinutes(1))
        ;

    final var result = Failsafe
        .with(breaker)
        .get(() -> {
          throw new RuntimeException("deu erro");
//          return "oi";
        });


    System.out.println(result);

  }
}
