package com.mageddo.resilience4j.ratelimiter;

import java.time.Duration;
import java.util.List;

import com.mageddo.RetryableException;

import org.junit.jupiter.api.Test;

import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class RateLimiterTest {
  @Test
  void mustLimitCalls() {
    var rl = RateLimiter.of("test", RateLimiterConfig
        .custom()
        .limitForPeriod(1)
        .limitRefreshPeriod(Duration.ofSeconds(60))
        .timeoutDuration(Duration.ZERO)
        .build());

    final var decorated = RateLimiter.decorateRunnable(rl, () -> {
    });

    decorated.run();
    assertThatThrownBy(decorated::run)
        .isInstanceOf(RequestNotPermitted.class);
  }

  @Test
  void mustThrowCustomException() {
    final var rateLimiter = RateLimiter.of("test", RateLimiterConfig
        .custom()
        .limitForPeriod(1)
        .limitRefreshPeriod(Duration.ofSeconds(60))
        .timeoutDuration(Duration.ZERO)
        .build());

    final var supplier = Decorators
        .ofSupplier(() -> 1)
        .withRateLimiter(rateLimiter)
        .withFallback(
            List.of(RequestNotPermitted.class),
            ex -> {
              throw new RetryableException("limite excedido", ex);
            }
        )
        .decorate();

    supplier.get();
    assertThatThrownBy(supplier::get)
        .isInstanceOf(RetryableException.class);
  }

}
