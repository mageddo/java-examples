package com.mageddo.kafka;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.Fallback;

import java.util.ArrayList;

import static com.mageddo.kafka.RetryPolicyConverter.retryPolicyToFailSafeRetryPolicy;

public class Retrier {
  public void run(
      final RetryPolicy retryPolicy,
      final Callback run,
      final Callback onRetry,
      final Callback onExhausted
  ) {
    Failsafe
        .with(
            Fallback.ofAsync(it -> {
              run.call();
              return null;
            }),
            retryPolicyToFailSafeRetryPolicy(retryPolicy)
                .onRetry(it -> onRetry.call())
                .handle(new ArrayList<>(retryPolicy.getRetryableExceptions()))
        )
        .run(ctx -> onExhausted.call());
  }
}
