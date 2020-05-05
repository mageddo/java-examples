package com.mageddo.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumeCallback<K, V> {
  void accept(Consumer<K,V> consumer, ConsumerRecord<K, V> record, Exception error);
}
