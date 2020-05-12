package com.mageddo.service;

import java.util.List;

import javax.inject.Singleton;
import javax.transaction.Transactional;

import com.mageddo.domain.Stock;

import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class StockPriceService {

  private final StockPriceDao stockPriceDao;

  @Transactional
  public void createStock(List<Stock> stocks){
    stocks.forEach(it -> this.stockPriceDao.createStock(it));
  }

  @Transactional
  public void updateStockPrice(Stock stock){
    this.stockPriceDao.updateStockPrice(stock);
  }

  public List<Stock> find() {
    return this.stockPriceDao.find();
  }
}
