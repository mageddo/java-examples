package com.mageddo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.gruelbox.transactionoutbox.TransactionManager;
import com.gruelbox.transactionoutbox.TransactionOutbox;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
public class MessageSender {

  private final KafkaProducer<String, byte[]> producer;
  private final ObjectMapper objectMapper;
  private final TransactionOutbox transactionOutbox;
  private final TransactionManager transactionManager;


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

  private void sendSync(ProducerRecord<String, byte[]> record) {
    try {
      this.producer.send(record).get();
    } catch (InterruptedException | ExecutionException e) {
      this.fallbackSend(record);
    }
  }

  private void fallbackSend(ProducerRecord<String, byte[]> record) {
    final var recordVO = RecordVO.builder()
        .topic(record.topic())
        .key(record.key())
        .value(record.value())
        .headers(HeadersConverter.encodeBase64(record.headers()))
        .build();

    this.transactionManager.inTransaction((t) -> {

      this.transactionOutbox.schedule(ProducerRecordDelegate.class)
          .sendSync(recordVO)
      ;
    });
  }
}
