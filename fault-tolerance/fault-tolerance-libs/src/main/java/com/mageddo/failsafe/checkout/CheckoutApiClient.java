package com.mageddo.failsafe.checkout;

import java.io.IOException;
import java.io.UncheckedIOException;

import dev.failsafe.Failsafe;
import dev.failsafe.function.ContextualSupplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckoutApiClient {

  public static String checkout(){
    return Failsafe
        .with(RetryPolicies.fast())
        .onComplete(it -> log.info("on sucess or error :" + it))
        .onFailure(it -> log.info("after all failures: " + it))
        .onSuccess(it -> log.info("success: " + it))
        .get(doCheckout());
  }

  static ContextualSupplier<String, String> doCheckout() {
    return (ctx) -> {
      log.info(
          "trying, attempt={}, attemptTime={}, totalTime={}",
          ctx.getAttemptCount(),
          ctx.getElapsedAttemptTime().toMillis(),
          ctx.getElapsedTime().toMillis()
      );
      throw new UncheckedIOException(new IOException("File Not Found o.o!"));
    };
  }
}
