package com.mageddo.kafka;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

//@Value
//@Builder
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ConsumerConfig<K, V> implements ConsumerCreateConfig<K, V>, ConsumingConfig<K, V> {

  public static final int FPS_30 = 1000 / 30;

  @Getter
  private Map<String, Object> props = new HashMap<>();

  @NonNull
  private String groupId;

  @NonNull
  private Collection<String> topics;

  @NonNull
  private Duration timeout = Duration.ofMillis(100);

  @NonNull
  private Duration interval = Duration.ofMillis(FPS_30);

  private RecoverCallback<K, V> recoverCallback;

  private ConsumeCallback<K, V> callback;

  private BatchConsumeCallback<K, V> batchCallback;

  public ConsumerConfig<K, V> withProp(String k, Object v) {
    this.props.put(k, v);
    return this;
  }

}
