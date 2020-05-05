package com.mageddo.failsafe;

import java.time.Duration;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.Fallback;
import net.jodah.failsafe.RetryPolicy;

public class Main {
  public static void main(String[] args) {
    Failsafe
        .with(
            Fallback.ofAsync(it -> {
              System.out.println("suppressing error");
            }),
            new RetryPolicy<>()
                .withMaxAttempts(3)
                .withDelay(Duration.ofSeconds(3))
                .handle(RuntimeException.class)
                .onRetriesExceeded(it -> System.out.println("retries exceeded: " + it))
                .onRetry(it -> System.out.println("retry: " + it))
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
