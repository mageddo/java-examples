package com.mageddo.transactional.outbox.tobby;

import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import lombok.RequiredArgsConstructor;

@Slf4j
@RequiredArgsConstructor
public class MessageSender {

  private final KafkaProducer<String, byte[]> producer;
  private final Producer<String, byte[]> fallbackProducer;
  private final ObjectMapper objectMapper;

  public void send(Fruit fruit) {
    try {
      final var record = new ProducerRecord<>(
          "outboxapp_fruit_topic",
          "key",
          this.objectMapper.writeValueAsBytes(fruit)
      );
      record.headers().add("version", "1".getBytes());
      this.sendSync(record);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendSync(ProducerRecord<String, byte[]> record) {
    try {
      this.producer.send(record).get();
      log.info("status=primarySent");
    } catch (InterruptedException | ExecutionException e) {
      this.sendSyncUsingFallback(record);
      throw new RuntimeException(e);
    }
  }

  @SneakyThrows
  private void sendSyncUsingFallback(ProducerRecord<String, byte[]> record) {
    this.fallbackProducer.send(record).get();
    log.info("status=fallbackSent");
  }
}
