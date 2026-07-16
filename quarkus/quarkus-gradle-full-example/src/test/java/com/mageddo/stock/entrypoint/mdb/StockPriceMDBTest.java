package com.mageddo.stock.entrypoint.mdb;

import java.math.BigDecimal;

import jakarta.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.stock.Stock;
import com.mageddo.stock.StockPriceDAO;

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
  StockPriceDAO stockPriceDao;

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
    final var foundStock = this.stockPriceDao.find("PAGS");
    assertNotNull(foundStock);
    assertEquals(new BigDecimal("10.00"), foundStock.getPrice());
  }
}
