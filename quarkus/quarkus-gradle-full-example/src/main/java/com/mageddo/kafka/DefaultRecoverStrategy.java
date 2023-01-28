package com.mageddo.kafka;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mageddo.kafka.client.RecoverContext;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import lombok.SneakyThrows;

@Singleton
public class DefaultRecoverStrategy {
  private final Producer<String, byte[]> producer;
  @Inject
  public DefaultRecoverStrategy(Producer<String, byte[]> producer) {
    this.producer = producer;
  }

  @SneakyThrows
  public void recover(RecoverContext<String, byte[]> ctx, String dlqName) {
    this.producer
        .send(new ProducerRecord<>(
            dlqName,
            ctx
                .record()
                .key(),
            ctx
                .record()
                .value()
        ))
        .get();
  }
}
