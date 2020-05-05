package com.mageddo.kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.Fallback;
import net.jodah.failsafe.RetryPolicy;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;

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
    final ConsumerConfig<String, byte[]> consumerConfig = new ConsumerConfig<>();
    consumerConfig.
    final Consumer<String, byte[]> consumer = consumerFactory.create(
        Map.of(GROUP_ID_CONFIG, "stock_client"), "stock_changed"
    );
    consumerFactory.poll(new ConsumingConfig<>(consumer, (records, e) -> {
      for (final var record : records) {
        Failsafe
            .with(
                Fallback.ofAsync(it -> {
                  log.info("exhausted tries....: {}", it);
                }),
                new RetryPolicy<>()
                    .withMaxAttempts(2)
                    .withDelay(Duration.ofSeconds(60 * 4))
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
              throw new RuntimeException("failed to consume");
//              log.info("key={}, value={}", record.key(), new String(record.value()));
            });
        ;
      }
      consumer.commitSync();
    }, Duration.ofMillis(100), Duration.ofMillis(1000 / 30)));
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
