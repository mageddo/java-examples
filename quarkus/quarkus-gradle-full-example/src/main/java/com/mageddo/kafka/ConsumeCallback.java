package com.mageddo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;

public interface ConsumeCallback<K, V> {
  void accept(ConsumerRecords<K, V> records, Exception error);
}
