package com.mageddo.resilience4j.ratelimiter;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.mageddo.RetryableException;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;

import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * https://chatgpt.com/c/6925d410-b908-8330-b0c9-894747024555
 */
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

  @Test
  void mustWaitBeforeCallAgain() {
    final var rateLimiter = RateLimiter.of("test", RateLimiterConfig
        .custom()
        .limitForPeriod(1)
        .limitRefreshPeriod(Duration.ofSeconds(5))
        .timeoutDuration(Duration.ofSeconds(6))
        .build());

    final var stopWatch = StopWatch.createStarted();
    final var supplier = Decorators
        .ofSupplier(() -> 1)
        .withRateLimiter(rateLimiter)
        .withFallback(
            List.of(RequestNotPermitted.class),
            ex -> {
              throw new RetryableException("limite excedido 2", ex);
            }
        )
        .decorate();

    supplier.get();
    supplier.get();
    assertThat(stopWatch.getTime()).isGreaterThanOrEqualTo(4_000);

  }

  @Test
  void mustUseRateLimiterWithRetry() {
    final var rateLimiter = RateLimiter.of("test", RateLimiterConfig
        .custom()
        .limitForPeriod(1)
        .limitRefreshPeriod(Duration.ofSeconds(5))
        .timeoutDuration(Duration.ZERO)
        .build());
    final var retryConfig = RetryConfig
        .custom()
        .maxAttempts(3)
        .waitDuration(Duration.ofSeconds(6))
        .consumeResultBeforeRetryAttempt((integer, o) -> {
          System.out.println("tentando.....");
        })
        .retryExceptions(RetryableException.class)
        .build();
    final var retry = Retry.of("external-api", retryConfig);

    final var tries = new AtomicInteger();
    final var supplier = Decorators
        .ofSupplier(() -> {
          tries.getAndIncrement();
          throw new RetryableException("limite excedido");
        })
        .withRateLimiter(rateLimiter)
        .withRetry(retry)
        .withFallback(
            List.of(RequestNotPermitted.class),
            ex -> {
              throw new RetryableException("limite excedido 2", ex);
            }
        )
        .decorate();

    final var stopWatch = StopWatch.createStarted();

    assertThatThrownBy(supplier::get)
        .isInstanceOf(RetryableException.class);

    assertThat(stopWatch.getTime()).isGreaterThanOrEqualTo(10_000);
    assertThat(stopWatch.getTime()).isLessThan(13_000);

    assertThat(tries.get()).isEqualTo(3);


  }

}
