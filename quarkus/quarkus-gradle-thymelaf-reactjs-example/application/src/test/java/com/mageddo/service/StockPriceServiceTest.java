package com.mageddo.service;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import com.mageddo.usecase.domain.Stock;
import com.mageddo.usecase.exception.DuplicatedStockException;
import com.mageddo.usecase.service.StockPriceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.quarkus.test.junit.QuarkusTest;
import testing.DatabaseConfigurator;
import testing.SingleInstancePostgresExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SingleInstancePostgresExtension.class)
@QuarkusTest
class StockPriceServiceTest {

  @Inject
  StockPriceService stockPriceService;

  @Inject
  DatabaseConfigurator databaseConfigurator;

  @BeforeEach
  void before(){
    this.databaseConfigurator.truncateTables();
  }

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

  @Test
  void mustGetErrorOnInsertAndRollbackJustTheSecondWhenUsingNestedPropagation() {

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
    this.stockPriceService.createStockNested(stocks);

    // assert
    assertEquals(1, this.stockPriceService.find().size());

  }
}
