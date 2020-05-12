package com.mageddo.service;

import java.util.List;

import javax.inject.Singleton;

import com.mageddo.domain.Stock;

import com.mageddo.jdbi.Transactional;

import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class StockPriceService {

  private final StockPriceDao stockPriceDao;

  @Transactional
  public void createStock(List<Stock> stocks){
    stocks.forEach(this.stockPriceDao::createStock);
  }

  @Transactional
  public void updateStockPrice(Stock stock){
    this.stockPriceDao.updateStockPrice(stock);
  }

  public List<Stock> find() {
    return this.stockPriceDao.find();
  }
}
