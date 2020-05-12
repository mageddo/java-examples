package com.mageddo.service;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import com.mageddo.domain.Stock;
import com.mageddo.exception.DuplicatedStockException;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class StockPriceServiceTest {

  @Inject
  StockPriceService stockPriceService;

  /**
   * Must rollback because it is trying to create PAGS symbol twice
   */
  @Test
  void mustGetErrorOnInsertAndRollbackAllRecords() {

    // arrange
    final var stocks = List.of(
        Stock
            .builder()
            .price(BigDecimal.TEN)
            .symbol("PAGS")
            .build(),
        Stock
            .builder()
            .price(BigDecimal.ONE)
            .symbol("PAGS")
            .build()
    );

    // act
    assertThrows(DuplicatedStockException.class, () -> {
      this.stockPriceService.createStock(stocks);
    });

    // assert
    assertTrue(this.stockPriceService.find().isEmpty());

  }
}
