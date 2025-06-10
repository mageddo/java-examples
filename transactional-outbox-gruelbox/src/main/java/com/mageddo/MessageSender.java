package com.mageddo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.gruelbox.transactionoutbox.TransactionOutbox;

import org.apache.kafka.clients.producer.ProducerRecord;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageSender {

  private final TransactionOutbox transactionOutbox;
  private final ObjectMapper objectMapper;

  public void send(Fruit fruit) {
    try {
      final var record = new ProducerRecord<>(
          "outboxapp_fruit_topic",
          "key",
          this.objectMapper.writeValueAsBytes(fruit)
      );
      record.headers().add("version", "1".getBytes());
      this.transactionOutbox.schedule(ProducerRecordDelegate.class)
          .sendSync(record)
      ;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
