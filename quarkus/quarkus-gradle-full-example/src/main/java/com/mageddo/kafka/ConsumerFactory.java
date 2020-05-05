package com.mageddo.kafka;

import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ConsumerFactory {

  public <K, V> void consume(ConsumerConfig<K, V> consumerConfig) {
    final Consumer<K, V> consumer = this.create(consumerConfig.getConsumerCreateConfig());
    this.poll(consumer, consumerConfig.getConsumingConfig());
  }

  public <K, V> KafkaConsumer<K, V> create(final ConsumerCreateConfig<K, V> consumerCreateConfig) {
    final KafkaConsumer<K, V> consumer = new KafkaConsumer<>(consumerCreateConfig.getProps());
    consumer.subscribe(consumerCreateConfig.getTopics());
    return consumer;
  }

  public <K, V> void poll(Consumer<K, V> consumer, ConsumingConfig<K, V> consumingConfig) {
    while (true) {
      try {
        final var records = consumer.poll(consumingConfig.getTimeout());
        if (log.isTraceEnabled()) {
          log.trace("status=polled, records={}", records.count());
        }
        consumingConfig
            .getCallback()
            .accept(records, null);
      } catch (Exception e) {
        if (log.isTraceEnabled()) {
          log.trace("status=poll-error", e);
        }
        consumingConfig
            .getCallback()
            .accept(null, e);
      }
      try {
        TimeUnit.MILLISECONDS.sleep(
            consumingConfig
                .getInterval()
                .toMillis()
        );
      } catch (InterruptedException e) {
        Thread
            .currentThread()
            .interrupt();
        break;
      }
    }
  }
}
