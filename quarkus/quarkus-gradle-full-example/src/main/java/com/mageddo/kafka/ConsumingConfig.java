package com.mageddo.kafka;

import java.time.Duration;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Delegate;

@Value
@Builder
public class ConsumingConfig<K, V> {

  public static final int FPS_30 = 1000 / 30;

  /**
   * The callback which will be called after poll the message
   */
  @NonNull
  private final ConsumeCallback<K, V> callback;

  /**
   * How long to wait the poll call
   */
  @NonNull
  @Builder.Default
  private final Duration timeout = Duration.ofMillis(100);

  /**
   * The interval between poll calls
   */
  @NonNull
  @Builder.Default
  private final Duration interval = Duration.ofMillis(FPS_30);

}
