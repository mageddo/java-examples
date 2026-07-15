package com.mageddo.service;

import java.util.List;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import com.mageddo.domain.Stock;

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
        .filter(this.stockPriceDao::createIfAbsent)
        .count();
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
