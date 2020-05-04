package com.mageddo.micronaut.openapi;

import java.util.concurrent.Future;

import javax.inject.Singleton;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import io.micronaut.configuration.kafka.annotation.KafkaClient;

@Singleton
public class KafkaProducer {

  private final Producer<String, byte[]> producer;

  public KafkaProducer(@KafkaClient("vanilla") Producer<String, byte[]> producer) {
    this.producer = producer;
  }

  public Future<RecordMetadata> send(ProducerRecord<String, byte[]> record) {
    return producer.send(record);
  }
}
