package com.mageddo.kafka;

import java.time.Duration;

import javax.enterprise.context.ApplicationScoped;

import com.mageddo.kafka.client.BatchConsumeCallback;
import com.mageddo.kafka.client.ConsumerConfig;
import com.mageddo.kafka.client.Consumers;
import com.mageddo.kafka.client.RecoverCallback;
import com.mageddo.kafka.client.RetryPolicy;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class StockPriceMDB {

  public static final String EVERY_5_SECONDS = "0/5 * * * * ?";

  private final Producer<String, byte[]> producer;

  public StockPriceMDB(Producer<String, byte[]> producer, ConsumerConfig<String, byte[]> consumerConfig) {
    this.producer = producer;
    Consumers.consume(consumerConfig
        .toBuilder()
        .topics("stock_changed")
        .consumers(3)
        .retryPolicy(RetryPolicy
            .builder()
            .maxTries(3)
            .delay(Duration.ofSeconds(29))
            .build()
        )
        .recoverCallback(recover())
        .batchCallback(consume())
        .build());
  }

  BatchConsumeCallback<String, byte[]> consume() {
    return (consumer, records, e) -> {
      for (final var record : records) {
//            throw new RuntimeException("an error occurred");
        log.info("key={}, value={}", record.key(), new String(record.value()));
      }
    };
  }

  RecoverCallback<String, byte[]> recover() {
    return (record, lastFailure) -> {};
  }

  @Scheduled(cron = EVERY_5_SECONDS)
  void notifyStockUpdates(ScheduledExecution execution) {
    producer.send(new ProducerRecord<>(
        "stock_changed",
        String.format("stock=PAGS, price=%.2f", Math.random() * 100)
            .getBytes()
    ));
    log.info(
        "status=scheduled, scheduled-fire-time={}, fire-time={}",
        execution.getScheduledFireTime(),
        execution.getFireTime()
    );
  }
}
