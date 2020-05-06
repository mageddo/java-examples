package com.mageddo.kafka;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.context.ApplicationScoped;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.Fallback;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import lombok.extern.slf4j.Slf4j;

import static com.mageddo.kafka.RetryPolicyConverter.retryPolicyToFailSafeRetryPolicy;
import static org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG;

@Slf4j
@ApplicationScoped
public class ConsumerFactory {

  public <K, V> void consume(ConsumerConfig<K, V> consumerConfig) {
    this.checkReasonablePollInterval(consumerConfig);
    final Consumer<K, V> consumer = this.create(consumerConfig);
    this.poll(consumer, consumerConfig);
  }

  public <K, V> KafkaConsumer<K, V> create(final ConsumerCreateConfig<K, V> consumerCreateConfig) {
    final KafkaConsumer<K, V> consumer = new KafkaConsumer<>(consumerCreateConfig.getProps());
    consumer.subscribe(consumerCreateConfig.getTopics());
    return consumer;
  }

  public <K, V> void poll(Consumer<K, V> consumer, ConsumingConfig<K, V> consumingConfig) {
    if (consumingConfig.getBatchCallback() == null && consumingConfig.getCallback() == null) {
      throw new IllegalArgumentException("You should inform BatchCallback Or Callback");
    }
    final boolean batchConsuming = consumingConfig.getBatchCallback() != null;
    while (true) {
      try {
        final var records = consumer.poll(consumingConfig.getTimeout());
        if (log.isTraceEnabled()) {
          log.trace("status=polled, records={}", records.count());
        }
        this.consume(consumer, consumingConfig, records, batchConsuming);
      } catch (Exception e) {
        log.warn("status=consuming-error", e);
        if (batchConsuming) {
          consumingConfig
              .getBatchCallback()
              .accept(consumer, null, e);
        } else {
          consumingConfig
              .getCallback()
              .accept(consumer, null, e);
        }
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
      ConsumerRecords<K, V> records,
      boolean batchConsuming
  ) {

    if (log.isTraceEnabled()) {
      log.trace("batch-consuming={}, records={}", batchConsuming, records.count());
    }
    if (batchConsuming) {
      this.doBatchConsume(consumer, consumingConfig, records);
    } else {
      this.doConsume(consumer, consumingConfig, records);
    }

  }

  private <K, V> void doBatchConsume(
      Consumer<K, V> consumer,
      ConsumingConfig<K, V> consumingConfig,
      ConsumerRecords<K, V> records
  ) {

    Failsafe
        .with(
            Fallback.ofAsync(it -> {
              log.info("status=exhausted-tries: {}, records={}", it, records.count());
              records.forEach(record -> this.doRecoverWhenAvailable(consumer, consumingConfig, record));
            }),
            retryPolicyToFailSafeRetryPolicy(consumingConfig.getRetryPolicy())
                .onRetry(it -> {
                  log.info("failed to consume: {}", it);
                  final Set<TopicPartition> partitions = records.partitions();
                  for (final TopicPartition partition : partitions) {
                    final ConsumerRecord<K, V> firstRecord = getFirstRecord(records, partition);
                    if (firstRecord != null) {
                      this.commitSyncRecord(consumer, firstRecord);
                    }
                  }
                })
                .handle(new ArrayList<>(consumingConfig.getRetryPolicy()
                    .getRetryableExceptions()))
        )
        .run(ctx -> {
          if (log.isTraceEnabled()) {
            log.debug("status=consuming, records={}", records);
          }
          consumingConfig
              .getBatchCallback()
              .accept(consumer, records, null);
        });
    consumer.commitSync();
  }

  private <K, V> void doConsume(
      Consumer<K, V> consumer,
      ConsumingConfig<K, V> consumingConfig,
      ConsumerRecords<K, V> records
  ) {
    for (final ConsumerRecord<K, V> record : records) {
      final AtomicBoolean recovered = new AtomicBoolean();
      Failsafe
          .with(
              Fallback.ofAsync(it -> {
                log.info("exhausted tries....: {}", it);
                this.doRecoverWhenAvailable(consumer, consumingConfig, record);
                recovered.set(true);
              }),
              retryPolicyToFailSafeRetryPolicy(consumingConfig.getRetryPolicy())
                  .onRetry(it -> {
                    log.info("failed to consume: {}", it);
                    this.commitSyncRecord(consumer, record);
                  })
                  .handle(new ArrayList<>(consumingConfig.getRetryPolicy()
                      .getRetryableExceptions()))
          )
          .run(ctx -> {
            if (log.isTraceEnabled()) {
              log.info("status=consuming, record={}", record);
            }
            consumingConfig
                .getCallback()
                .accept(consumer, record, null);
          });
      if (recovered.get()) {
        // pare o consumo para fazer poll imediatamente
        // e não chegar no timeout por não ter chamado poll
        // por causa das retentativas dessa mensagem
        return;
      }
    }
    consumer.commitSync();
  }

  private <K, V> void doRecoverWhenAvailable(
      Consumer<K, V> consumer,
      ConsumingConfig<K, V> consumingConfig,
      ConsumerRecord<K, V> record
  ) {
    if(consumingConfig.getRecoverCallback() != null){
      consumingConfig.getRecoverCallback().recover(record);
      this.commitSyncRecord(consumer, record);
    } else {
      log.warn("status=no recover callback was specified");
    }
  }

  private <K, V> void commitSyncRecord(Consumer<K, V> consumer, ConsumerRecord<K, V> record) {
    consumer.commitSync(Collections.singletonMap(
        new TopicPartition(record.topic(), record.partition()),
        new OffsetAndMetadata(record.offset())
    ));
  }

  private <K, V> ConsumerRecord<K, V> getFirstRecord(ConsumerRecords<K, V> records, TopicPartition partition) {
    final List<ConsumerRecord<K, V>> partitionRecords = records.records(partition);
    return partitionRecords.isEmpty() ? null : partitionRecords.get(0);
  }

  private <V, K> void checkReasonablePollInterval(ConsumerConfig<K, V> consumerConfig) {
    final int defaultPollInterval = (int) Duration
        .ofMinutes(5)
        .toMillis();

    final int currentPollInterval = (int) consumerConfig
        .getProps()
        .getOrDefault(MAX_POLL_INTERVAL_MS_CONFIG, defaultPollInterval);

    final RetryPolicy retryPolicy = consumerConfig.getRetryPolicy();

    final long retryMaxWaitTime = retryPolicy
        .calcMaxTotalWaitTime()
        .toMillis();

    if (currentPollInterval < retryMaxWaitTime) {
      log.warn(
          "msg=your 'max.poll.interval.ms' is set to a value less than the retry policy, it will cause consumer "
              + "rebalancing, increase 'max.poll.interval.ms' or decrease the retry policy delay or retries, "
              + "max.poll.interval.ms={}, retryMaxWaitTime={} (retries={}, delay={})",
          Duration.ofMillis(currentPollInterval),
          Duration.ofMillis(retryMaxWaitTime),
          retryPolicy.getMaxTries(),
          retryPolicy.getDelay()
      );
    }
  }
}
