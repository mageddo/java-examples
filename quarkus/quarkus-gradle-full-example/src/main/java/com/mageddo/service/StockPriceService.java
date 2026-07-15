package com.mageddo.service;

import java.util.List;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import com.mageddo.domain.Stock;
import com.mageddo.exception.DuplicatedStockException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class StockPriceService {

  private final StockPriceDao stockPriceDao;

  @Transactional
  public void createStock(List<Stock> stocks){
    stocks.forEach(this.stockPriceDao::createIfAbsent);
  }

  @Transactional
  public void updateStockPrice(Stock stock){
    this.stockPriceDao.updateStockPrice(stock);
  }

  public List<Stock> find() {
    return this.stockPriceDao.find();
  }

  @Transactional
  public void createStockNested(List<Stock> stocks) {
    stocks.forEach(it -> {
      try {
        self().createStockNested(it);
      } catch (DuplicatedStockException e) {
        log.info("status=already-exists, stock={}", it.getSymbol());
      }
    });
  }

  public void createStockNested(Stock stock) {
    this.stockPriceDao.createIfAbsent(stock);
  }

  StockPriceService self(){
    return CDI.current()
        .select(StockPriceService.class)
        .get();
  }
}
