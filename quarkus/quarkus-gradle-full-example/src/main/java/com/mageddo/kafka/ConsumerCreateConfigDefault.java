package com.mageddo.kafka;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class ConsumerCreateConfigDefault<K, V> implements ConsumerCreateConfig<K, V> {

  @Getter
  private Map<String, Object> props = new HashMap<>();

  @NonNull
  private String groupId;

  @NonNull
  private Collection<String> topics;

  private RecoverCallback<K, V> recoverCallback;

  public ConsumerCreateConfigDefault<K, V> withProp(String k, Object v) {
    this.props.put(k, v);
    return this;
  }
}
