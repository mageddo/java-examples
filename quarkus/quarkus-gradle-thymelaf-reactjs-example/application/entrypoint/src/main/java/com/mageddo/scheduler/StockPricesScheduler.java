package com.mageddo.scheduler;

import java.math.BigDecimal;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.usecase.domain.Stock;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class StockPricesScheduler {

  public static final String EVERY_5_SECONDS = "0/5 * * * * ?";
  private final Producer<String, byte[]> producer;
  private final ObjectMapper objectMapper;

  @SneakyThrows
  @Scheduled(cron = EVERY_5_SECONDS)
  void notifyStockUpdates(ScheduledExecution execution) {

    final var stock = Stock
        .builder()
        .symbol(RandomStringUtils.random(3, 'A', 'B', 'C')
            .toUpperCase())
        .price(BigDecimal.valueOf(Math.random() * 100))
        .build();
    this.producer.send(new ProducerRecord<>("stock_changed_v3", this.objectMapper.writeValueAsBytes(stock)));
    log.info(
        "status=scheduled, symbol={}, scheduled-fire-time={}, fire-time={}",
        stock.getSymbol(),
        execution.getScheduledFireTime(),
        execution.getFireTime()
    );
  }

}
