package com.mageddo.kafka;

import java.time.Duration;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class ConsumingConfigDefault<K, V> implements ConsumingConfig<K, V> {

  public static final int FPS_30 = 1000 / 30;
  public static final Duration FPS_30_DURATION = Duration.ofMillis(FPS_30);
  public static final Duration DEFAULT_POLL_TIMEOUT = Duration.ofMillis(100);
  public static final RetryStrategy DEFAULT_RETRY_STRATEGY = RetryStrategy
      .builder()
      .delay(Duration.ofSeconds(5))
      .build();

  @NonNull
  @Builder.Default
  private Duration timeout = DEFAULT_POLL_TIMEOUT;

  @NonNull
  @Builder.Default
  private Duration interval = FPS_30_DURATION;

  @NonNull
  @Builder.Default
  private RetryStrategy retryStrategy = DEFAULT_RETRY_STRATEGY;

  private RecoverCallback<K, V> recoverCallback;

  private ConsumeCallback<K, V> callback;

  private BatchConsumeCallback<K, V> batchCallback;

}
