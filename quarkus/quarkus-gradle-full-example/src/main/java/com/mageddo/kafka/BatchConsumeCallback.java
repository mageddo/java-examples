package com.mageddo.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public interface BatchConsumeCallback<K, V> {
  void accept(Consumer<K, V> consumer, ConsumerRecords<K, V> records, Exception error);
}
