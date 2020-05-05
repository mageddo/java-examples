package com.mageddo.kafka;

import java.util.Collection;
import java.util.Map;

public interface ConsumerCreateConfig<K, V> {

  Map<String, Object> getProps();

  String getGroupId();

  Collection<String> getTopics();

  RecoverCallback<K, V> getRecoverCallback();

  ConsumerCreateConfig<K, V> withProp(String k, Object v);
}
