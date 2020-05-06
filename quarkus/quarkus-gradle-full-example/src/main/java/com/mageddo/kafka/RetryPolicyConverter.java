package com.mageddo.kafka;

import java.util.ArrayList;

public class RetryPolicyConverter {
  public static net.jodah.failsafe.RetryPolicy<?> retryPolicyToFailSafeRetryPolicy(RetryPolicy retryPolicy) {
    final net.jodah.failsafe.RetryPolicy<?> failSafeRetryPolicy = new net.jodah.failsafe.RetryPolicy<>();
    failSafeRetryPolicy
        .withMaxRetries(retryPolicy.getMaxTries())
        .withDelay(retryPolicy.getDelay())
    ;
    if(retryPolicy.getMaxDelay() != null){
      failSafeRetryPolicy.withMaxDuration(retryPolicy.getMaxDelay());
    }
    if(!retryPolicy.getRetryableExceptions().isEmpty()){
        failSafeRetryPolicy.handle(new ArrayList<>(retryPolicy.getRetryableExceptions()));
    }
    return failSafeRetryPolicy;
  }
}
