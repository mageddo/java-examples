package com.mageddo.kafka;

import java.time.Duration;
import java.util.Map;

import io.quarkus.runtime.StartupEvent;

import org.apache.kafka.clients.consumer.Consumer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

@ApplicationScoped
public class StockPriceMDB {

  @Inject
  ConsumerFactory consumerFactory;

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
}
