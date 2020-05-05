package com.mageddo.kafka;

import java.time.Duration;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class StockPriceMDB {

  public static final String EVERY_5_SECONDS = "0/5 * * * * ?";

  private final ConsumerFactory consumerFactory;
  private final Producer<String, byte[]> producer;

  public void consume(@Observes StartupEvent ev) {
    log.info("status=consume fired");
    final Consumer<String, byte[]> consumer = consumerFactory.create(
        Map.of(GROUP_ID_CONFIG, "stock_client"), "stock_changed"
    );
    consumerFactory.poll(consumer, (records, e) -> {
      for (final var record : records) {
        log.info("key={}, value={}", record.key(), new String(record.value()));
      }
      consumer.commitSync();
    }, Duration.ofMillis(100), Duration.ofMillis(1000 / 30));
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
