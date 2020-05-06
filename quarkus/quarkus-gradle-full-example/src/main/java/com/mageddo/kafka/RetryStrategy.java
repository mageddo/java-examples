package com.mageddo.kafka;

import java.time.Duration;

import lombok.Builder;

@Builder
public class RetryStrategy {
  @Builder.Default
  private int maxTries = 2;
  private Duration delay;
  private Duration maxDelay;
}
