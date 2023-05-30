package com.mageddo.failsafe;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Duration;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;

import dev.failsafe.Failsafe;
import dev.failsafe.RetryPolicy;
import lombok.extern.slf4j.Slf4j;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class RetryTest {

  @Test
  void mustRetryUntilExhaust(){

    // arrange
    final var stopWatch = StopWatch.createStarted();

    // act
    assertThrows(UncheckedIOException.class, () -> {
    Failsafe
        .with(
            RetryPolicy.builder()
                .withMaxAttempts(3)
                .withDelay(Duration.ofMillis(300))
                .handle(UncheckedIOException.class)
                .onRetriesExceeded(it -> log.info("retries exceeded: " + it))
                .onRetry(it -> log.info("retry: " + it))
                .build()
        )
        .onComplete(it -> log.info("on sucess or error :" + it))
        .onFailure(it -> log.info("after all failures: " + it))
        .onSuccess(it -> log.info("success: " + it))
        .get((ctx) -> {
          log.info(
              "trying, attempt={}, attemptTime={}, totalTime={}",
              ctx.getAttemptCount(),
              ctx.getElapsedAttemptTime().toMillis(),
              ctx.getElapsedTime().toMillis()
          );
          throw new UncheckedIOException(new IOException("File Not Found o.o!"));
        });
    });

    // assert
    assertTrue(stopWatch.getTime() >= 600);
  }
}
