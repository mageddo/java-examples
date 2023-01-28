package com.mageddo.mdb;

import java.math.BigDecimal;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.domain.Stock;
import com.mageddo.service.StockPriceDao;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.quarkus.test.junit.QuarkusTest;
import templates.ConsumerRecordsTemplates;
import testing.SingleInstancePostgresExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SingleInstancePostgresExtension.class)
@QuarkusTest
public class StockPriceMDBTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Inject
  StockPriceMDB stockPriceMDB;

  @Inject
  StockPriceDao stockPriceDao;

  @Test
  void mustConsumeAndSave() throws Exception {

    // arrange
    final var stock = Stock
        .builder()
        .price(BigDecimal.TEN)
        .symbol("PAGS")
        .build();
    final ConsumerRecords<String, byte[]> consumerRecords = ConsumerRecordsTemplates.build(
        this.objectMapper.writeValueAsBytes(stock)
    );

    // act
    this.stockPriceMDB.consume(consumerRecords);

    // assert
    final var foundStock = this.stockPriceDao.getStock("PAGS");
    assertNotNull(foundStock);
    assertEquals(new BigDecimal("10.00"), foundStock.getPrice());
  }
}
