package com.mageddo;

import lombok.RequiredArgsConstructor;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
public class ProducerRecordDelegate {

  private final KafkaProducer<String, byte[]> producer;

  public void sendSync(ProducerRecord<String, byte[]> record) {
    try {
      record.headers().add("version", "1".getBytes());
      this.producer.send(record).get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }
}
