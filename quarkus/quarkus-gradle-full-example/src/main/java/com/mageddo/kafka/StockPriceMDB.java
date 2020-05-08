package com.mageddo.kafka;

import java.time.Duration;
import java.util.Collections;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import com.mageddo.kafka.client.ConsumerConfig;
import com.mageddo.kafka.client.Consumers;
import com.mageddo.kafka.client.RetryPolicy;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class StockPriceMDB {

  public static final String EVERY_5_SECONDS = "0/5 * * * * ?";

  private final Producer<String, byte[]> producer;
  private final ConsumerConfig<String, byte[]> consumerConfig;

  public void consume(@Observes StartupEvent ev) {
    Consumers.consume(this.consumerConfig
        .toBuilder()
        .topics("stock_changed")
        .consumers(3)
        .retryPolicy(RetryPolicy
            .builder()
            .maxTries(3)
            .delay(Duration.ofSeconds(29))
            .build()
        )
        .batchCallback((consumer, records, e) -> {
          for (final var record : records) {
//            throw new RuntimeException("an error occurred");
            log.info("key={}, value={}", record.key(), new String(record.value()));
          }
        })
        .build());
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
