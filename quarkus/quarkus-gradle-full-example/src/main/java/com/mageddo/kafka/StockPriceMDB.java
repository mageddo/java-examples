package com.mageddo.kafka;

import java.time.Duration;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;

import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

@ApplicationScoped
public class StockPriceMDB {

  @Inject
  ConsumerFactory consumerFactory;

  @Inject
  Producer<String, byte[]> producer;

  public void consume(@Observes StartupEvent ev) {
    final Consumer<String, byte[]> consumer = consumerFactory.create(
        Map.of(GROUP_ID_CONFIG, "stock_client"), "stock_changed"
    );
    consumerFactory.poll(consumer, ((records, e) -> {
      for (final var record : records) {
        System.out.printf("key=%s, value=%s%n", record.key(), new String(record.value()));
      }
      consumer.commitSync();
    }), Duration.ofMillis(100), Duration.ofMillis(1000 / 30));
  }

  @Scheduled(cron = "0/5 * * * * ?")
  void notifyStockUpdates(ScheduledExecution execution) {
    producer.send(new ProducerRecord<>(
        "stock_changed",
        String.format("stock=PAGS, price=%.2f", Math.random() * 100)
            .getBytes()
    ));
    System.out.println(execution.getScheduledFireTime());
  }
}
