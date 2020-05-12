package com.mageddo.service;

import javax.inject.Singleton;
import javax.transaction.Transactional;

import com.mageddo.domain.Stock;

import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class StockPriceService {

  private final StockPriceDao stockPriceDao;

  @Transactional()
  public void updateStockPrice(Stock stock){
    this.stockPriceDao.updateStockPrice(stock);
  }
}
