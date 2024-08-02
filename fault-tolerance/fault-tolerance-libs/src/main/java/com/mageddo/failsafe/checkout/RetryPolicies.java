package com.mageddo.failsafe.checkout;

import java.io.UncheckedIOException;
import java.time.Duration;

import dev.failsafe.RetryPolicy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RetryPolicies {
  public static RetryPolicy<Object> fast() {
    return RetryPolicy.builder()
        .withMaxAttempts(3)
        .withDelay(Duration.ofMillis(300))
        .handle(UncheckedIOException.class)
        .onRetriesExceeded(it -> log.info("retries exceeded: " + it))
        .onRetry(it -> log.info("retry: " + it))
        .build();
  }
}
