package com.mageddo.failsafe;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.CircuitBreakerOpenException;
import net.jodah.failsafe.Failsafe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CircuitBreakerTest {

  @Test
  void mustOpenThenHalfOpenTheCircuit() {

    final var stats = new Stats();

    final var breaker = new CircuitBreaker<String>()
        .handle(UncheckedIOException.class)
        .withFailureThreshold(3, 5)
        .withSuccessThreshold(5)
        .withDelay(Duration.ofMillis(500));

    calcStats(stats, () -> runSuccess(breaker));
    calcStats(stats, () -> runSuccess(breaker));
    calcStats(stats, () -> runSuccess(breaker));

    calcStats(stats, () -> runError(breaker));
    calcStats(stats, () -> runError(breaker));
    calcStats(stats, () -> runError(breaker));
    calcStats(stats, () -> runError(breaker));

    calcStats(stats, () -> runSuccess(breaker));

    sleep(1000);
    calcStats(stats, () -> runSuccess(breaker));
    calcStats(stats, () -> runSuccess(breaker));

    assertTrue(breaker.isHalfOpen());
    assertFalse(breaker.isOpen());
    assertFalse(breaker.isClosed());
    assertEquals(5, stats.success);
    assertEquals(3, stats.error);
    assertEquals(2, stats.openCircuit);
  }

  void calcStats(Stats stats, Runnable r) {
    try {
      r.run();
      stats.success++;
    } catch (CircuitBreakerOpenException e) {
      stats.openCircuit++;
    } catch (UncheckedIOException e) {
      stats.error++;
    }
  }

  String runError(CircuitBreaker<String> breaker) {
    return Failsafe.with(breaker)
        .get(() -> {
          throw new UncheckedIOException(new IOException("an error"));
        });
  }

  String runSuccess(CircuitBreaker<String> breaker) {
    return Failsafe.with(breaker)
        .get(() -> LocalDateTime.now().toString());
  }

  void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static class Stats {
    int success;
    int error;
    int openCircuit;
  }
}
