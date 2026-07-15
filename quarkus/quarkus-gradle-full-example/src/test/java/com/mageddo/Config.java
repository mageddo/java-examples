package com.mageddo;

import jakarta.enterprise.inject.Produces;

import com.mageddo.kafka.client.ConsumerConfig;

import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class Config {

  @Produces
  public ConsumerConfig<String, byte[]> consumerConfig() {
    return ConsumerConfig
        .<String, byte[]>builder()
        .consumers(Integer.MIN_VALUE)
        .build();
  }

  @Produces
  public Producer<String, byte[]> producer() {
    return new MockProducer<String, byte[]>(true, null, new StringSerializer(), new ByteArraySerializer());
  }
}
