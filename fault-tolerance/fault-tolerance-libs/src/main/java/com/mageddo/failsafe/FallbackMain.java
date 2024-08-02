package com.mageddo.failsafe;

import java.time.Duration;

import dev.failsafe.Failsafe;
import dev.failsafe.Fallback;
import dev.failsafe.RetryPolicy;

public class FallbackMain {
  public static void main(String[] args) {
    Failsafe
        .with(
            Fallback
                .builder(it -> {
                  System.out.println("suppressing error");
                })
                .withAsync()
                .build(),
            RetryPolicy.builder()
                .withMaxAttempts(3)
                .withDelay(Duration.ofSeconds(3))
                .handle(RuntimeException.class)
                .onRetriesExceeded(it -> System.out.println("retries exceeded: " + it))
                .onRetry(it -> System.out.println("retry: " + it))
                .build()
        )
        .onComplete(it -> System.out.println("completed :" + it))
        .onFailure(it -> System.out.println("failure: " + it))
        .onSuccess(it -> System.out.println("success: " + it))
        .run((ctx) -> {
          System.out.println("trying.....");
          throw new RuntimeException("an error occurred");
        })
    ;
  }
}
