package com.mageddo.failsafe;

import com.mageddo.concurrency.Threads;
import dev.failsafe.Failsafe;
import dev.failsafe.RateLimitExceededException;
import dev.failsafe.RateLimiter;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * https://failsafe.dev/rate-limiter/
 */
public class RateLimitTest {

  @Test
  void mustLimitRequestsAt3PerSecond() {
    // arrange
    RateLimiter<Object> limiter = RateLimiter.burstyBuilder(3, Duration.ofSeconds(1)).build();

    // act
    // assert
    this.runSuccess(limiter);
    this.runSuccess(limiter);
    this.runSuccess(limiter);
    assertThrows(RateLimitExceededException.class, () -> this.runSuccess(limiter));

    Threads.sleep(1001);

    this.runSuccess(limiter);
    this.runSuccess(limiter);
    this.runSuccess(limiter);
    assertThrows(RateLimitExceededException.class, () -> this.runSuccess(limiter));

  }

  void runSuccess(RateLimiter<Object> limiter) {
    Failsafe
        .with(limiter)
        .get(() -> LocalDateTime.now().toString());
  }

}
