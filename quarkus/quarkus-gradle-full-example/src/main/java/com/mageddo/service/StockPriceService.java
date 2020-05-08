package com.mageddo.service;

import com.mageddo.domain.Stock;

import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RequiredArgsConstructor
public class StockPriceService {

  private final StockPriceDao stockPriceDao;

  public void updateStockPrice(Stock stock){
    this.stockPriceDao.updateStockPrice(stock);
  }
}
