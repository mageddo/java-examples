package com.mageddo.stock;

import java.util.List;

import com.mageddo.exception.DuplicatedStockException;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class StockPriceService {

  private final StockPriceDAO stockPriceDao;

  @Transactional
  public long createIfAbsent(List<Stock> stocks) {
    return stocks.stream()
        .filter(this::createIfAbsent)
        .count();
  }

  @Transactional
  public void create(List<Stock> stocks) {
    stocks.forEach(stock -> {
      if (!this.createIfAbsent(stock)) {
        throw new DuplicatedStockException(stock.getSymbol());
      }
    });
  }

  @Transactional
  public void update(Stock stock) {
    this.stockPriceDao.update(stock);
  }

  public List<Stock> find() {
    return this.stockPriceDao.find();
  }

  public boolean createIfAbsent(Stock stock) {
    return this.stockPriceDao.createIfAbsent(stock);
  }

}
