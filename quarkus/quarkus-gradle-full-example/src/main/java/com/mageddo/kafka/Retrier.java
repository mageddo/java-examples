package com.mageddo.kafka;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.Fallback;
import net.jodah.failsafe.RetryPolicy;

public class Retrier {
  public void run(
      final RetryPolicy<?> retryPolicy,
      final Callback run,
      final Callback onRetry,
      final Callback onExhausted,
      final Class<Throwable>... exceptionsToHandle
  ) {
    Failsafe
        .with(
            Fallback.ofAsync(it -> {
              run.call();
              return null;
            }),
            retryPolicy
                .onRetry(it -> onRetry.call())
                .handle(exceptionsToHandle)
        )
        .run(ctx -> onExhausted.call());
  }
}
