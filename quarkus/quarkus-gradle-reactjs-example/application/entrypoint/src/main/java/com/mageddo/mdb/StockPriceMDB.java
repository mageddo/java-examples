package com.mageddo.mdb;

import java.time.Duration;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.kafka.client.BatchConsumeCallback;
import com.mageddo.kafka.client.Consumers;
import com.mageddo.kafka.client.RecoverCallback;
import com.mageddo.kafka.client.RetryPolicy;
import com.mageddo.usecase.domain.Stock;
import com.mageddo.usecase.service.StockPriceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class StockPriceMDB {

  private final Consumers<String, byte[]> consumers;
  private final StockPriceService stockPriceService;
  private final ObjectMapper objectMapper;

  @PostConstruct
  public void init() {
    this.consumers
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

}
