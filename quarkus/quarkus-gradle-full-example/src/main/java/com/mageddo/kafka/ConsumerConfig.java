package com.mageddo.kafka;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.Delegate;

//@Value
//@Builder
@NoArgsConstructor
public class ConsumerConfig<K, V> {

  @Delegate
  private ConsumingConfig<K, V> consumingConfig;

  @Delegate
  private ConsumerCreateConfig<K, V> consumerCreateConfig;

}
