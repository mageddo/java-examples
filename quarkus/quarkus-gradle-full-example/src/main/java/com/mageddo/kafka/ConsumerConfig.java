package com.mageddo.kafka;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ConsumerConfig<K, V> implements ConsumerCreateConfig<K, V>, ConsumingConfig<K, V> {

  @Getter
  private Map<String, Object> props = new HashMap<>();

  @NonNull
  private String groupId;

  @NonNull
  private Collection<String> topics;

  @NonNull
  private Duration timeout = ConsumingConfigDefault.DEFAULT_POLL_TIMEOUT;

  @NonNull
  private Duration interval = ConsumingConfigDefault.FPS_30_DURATION;

  @NonNull
  private RetryPolicy retryPolicy = ConsumingConfigDefault.DEFAULT_RETRY_STRATEGY;

  private RecoverCallback<K, V> recoverCallback;

  private ConsumeCallback<K, V> callback;

  private BatchConsumeCallback<K, V> batchCallback;

  public ConsumerConfig<K, V> withProp(String k, Object v) {
    this.props.put(k, v);
    return this;
  }

}
