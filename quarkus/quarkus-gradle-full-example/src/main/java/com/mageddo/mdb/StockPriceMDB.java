package com.mageddo.mdb;

import java.math.BigDecimal;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.domain.Stock;
import com.mageddo.kafka.Topics;
import com.mageddo.kafka.client.Consumer;
import com.mageddo.kafka.client.ConsumerConfig;
import com.mageddo.service.StockPriceService;

import org.apache.kafka.clients.consumer.ConsumerRecords;
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
public class StockPriceMDB implements Consumer {

  public static final String EVERY_5_SECONDS = "0/1 * * * * ?";

  private final Producer<String, byte[]> producer;
  private final StockPriceService stockPriceService;
  private final ObjectMapper objectMapper;

  @SneakyThrows
  public void consume(ConsumerRecords<String, byte[]> records) {
    for (final var record : records) {
      final var stock = this.objectMapper.readValue(record.value(), Stock.class);
      this.stockPriceService.updateStockPrice(stock);
      log.info("key={}, value={}", record.key(), new String(record.value()));
    }
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

  public Topics topic(){
    return Topics.STOCK_PRICE;
  }

  @Override
  public ConsumerConfig<?, ?> config() {
    return  this
        .topic()
        .toBatchConfig((ctx, records) -> this.consume(records));
  }
}
