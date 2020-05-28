package com.mageddo.scheduler;

import java.math.BigDecimal;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.usecase.domain.Stock;

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

  public static final String EVERY_5_SECONDS = "0/1 * * * * ?";
  private final Producer<String, byte[]> producer;
  private final ObjectMapper objectMapper;

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
}
