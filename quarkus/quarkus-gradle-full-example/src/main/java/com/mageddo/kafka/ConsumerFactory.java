package com.mageddo.kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.Fallback;
import net.jodah.failsafe.RetryPolicy;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ConsumerFactory {

  public <K, V> void consume(ConsumerConfig<K, V> consumerConfig) {
    final Consumer<K, V> consumer = this.create(consumerConfig);
    this.poll(consumer, consumerConfig);
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
        this.consume(consumer, consumingConfig, records);
      } catch (Exception e) {
        if (log.isTraceEnabled()) {
          log.trace("status=poll-error", e);
        }
        consumingConfig
            .getCallback()
            .accept(consumer, null, e);
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

  private <K, V> void consume(
      Consumer<K, V> consumer,
      ConsumingConfig<K, V> consumingConfig,
      ConsumerRecords<K, V> records
  ) {

    final RetryPolicy<?> retryPolicy = new RetryPolicy<>()
        .withMaxAttempts(2)
        .withDelay(Duration.ofSeconds(60 * 4));
    if (consumingConfig.getCallback() != null) {
      this.doConsume(consumer, consumingConfig, records, retryPolicy);
    } else {
      this.doBatchConsume(consumer, consumingConfig, records, retryPolicy);
    }

  }

  private <K, V> void doBatchConsume(
      Consumer<K, V> consumer,
      ConsumingConfig<K, V> consumingConfig,
      ConsumerRecords<K, V> records,
      RetryPolicy<?> retryPolicy
  ) {

    Failsafe
        .with(
            Fallback.ofAsync(it -> {
              log.info("exhausted tries....: {}", it);
              records.forEach(
                  record -> consumingConfig
                      .getRecoverCallback()
                      .recover(record)
              );
            }),
            retryPolicy
                .onRetry(it -> {
                  log.info("failed to consume: {}", it);
                  final Set<TopicPartition> partitions = records.partitions();
                  for (final TopicPartition partition : partitions) {
                    final ConsumerRecord<K, V> firstRecord = getFirstRecord(records, partition);
                    if (firstRecord != null) {
                      consumer.commitSync(Collections.singletonMap(
                          new TopicPartition(firstRecord.topic(), firstRecord.partition()),
                          new OffsetAndMetadata(firstRecord.offset())
                      ));
                    }
                  }
                })
                .handle(Exception.class)
        )
        .run(ctx -> {
          log.info("trying to consume: {}", records);
          consumingConfig
              .getBatchCallback()
              .accept(consumer, records, null);
        });
    consumer.commitSync();
  }

  private <K, V> void doConsume(
      Consumer<K, V> consumer,
      ConsumingConfig<K, V> consumingConfig,
      ConsumerRecords<K, V> records,
      RetryPolicy<?> retryPolicy
  ) {
    for (final ConsumerRecord<K, V> record : records) {
      Failsafe
          .with(
              Fallback.ofAsync(it -> {
                log.info("exhausted tries....: {}", it);
                consumingConfig.getRecoverCallback()
                    .recover(record);
              }),
              retryPolicy
                  .onRetry(it -> {
                    log.info("failed to consume: {}", it);
                    consumer.commitSync(Collections.singletonMap(
                        new TopicPartition(record.topic(), record.partition()),
                        new OffsetAndMetadata(record.offset())
                    ));
                  })
                  .handle(Exception.class)
          )
          .run(ctx -> {
            log.info("trying to consume: {}", record);
            consumingConfig
                .getCallback()
                .accept(consumer, record, null);
          });
    }
    consumer.commitSync();
  }

  private <K, V> ConsumerRecord<K, V> getFirstRecord(ConsumerRecords<K, V> records, TopicPartition partition) {
    final List<ConsumerRecord<K, V>> partitionRecords = records.records(partition);
    return partitionRecords.isEmpty() ? null : partitionRecords.get(0);
  }

}
