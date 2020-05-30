package com.mageddo.mdb;

import java.math.BigDecimal;
import java.time.Duration;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.domain.Stock;
import com.mageddo.kafka.client.BatchConsumeCallback;
import com.mageddo.kafka.client.ConsumerFactory;
import com.mageddo.kafka.client.Consumers;
import com.mageddo.kafka.client.RecoverCallback;
import com.mageddo.kafka.client.RetryPolicy;
import com.mageddo.service.StockPriceService;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class StockPriceMDB {

  public static final String EVERY_5_SECONDS = "0/1 * * * * ?";

  private final Producer<String, byte[]> producer;
  private final Consumers<String, byte[]> consumers;
  private final StockPriceService stockPriceService;
  private final ObjectMapper objectMapper;
  private ConsumerFactory<String, byte[]> consumerFactory;

  @PostConstruct
  public void init() {
    this.consumerFactory = this.consumers
        .toBuilder()
        .topics("stock_changed_v2")
        .prop(GROUP_ID_CONFIG, "quarkus_gradle_stock_changed_v2")
        .consumers(3)
        .retryPolicy(RetryPolicy
            .builder()
            .maxTries(3)
            .delay(Duration.ofSeconds(29))
            .build()
        )
        .recoverCallback(recover())
        .batchCallback(consume())
        .build()
        .consume();
  }

  BatchConsumeCallback<String, byte[]> consume() {
    return (ctx, records) -> {
      for (final var record : records) {
        final var stock = this.objectMapper.readValue(record.value(), Stock.class);
        this.stockPriceService.updateStockPrice(stock);
        log.info("key={}, value={}", record.key(), new String(record.value()));
      }
    };
  }

  RecoverCallback<String, byte[]> recover() {
    return (ctx) -> {
      log.error("status=exhausted, record={}", new String(ctx.record().value()), ctx.lastFailure());
    };
  }

  @SneakyThrows
  @Scheduled(cron = EVERY_5_SECONDS)
  void notifyStockUpdates(ScheduledExecution execution) {
    producer.send(new ProducerRecord<>(
        "stock_changed_v2",
        this.objectMapper.writeValueAsBytes(Stock
            .builder()
            .symbol("PAGS")
            .price(BigDecimal.valueOf(Math.random() * 100))
            .build()
        )
    ));
    log.info(
        "status=scheduled, scheduled-fire-time={}, fire-time={}",
        execution.getScheduledFireTime(),
        execution.getFireTime()
    );
  }

  public void close(@Observes ShutdownEvent event) throws Exception {
    this.consumerFactory.close();
  }
}
