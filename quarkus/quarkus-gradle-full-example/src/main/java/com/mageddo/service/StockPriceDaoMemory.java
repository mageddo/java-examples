package com.mageddo.service;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Singleton;

import com.mageddo.domain.Stock;

@Singleton
public class StockPriceDaoMemory implements StockPriceDao {

  private Map<String, Stock> stocks = new LinkedHashMap<>();

  @Override
  public void updateStockPrice(Stock stock) {
    this.stocks.put(stock.getSymbol(), stock);
  }

  @Override
  public Stock getStock(String symbol){
    return this.stocks.get(symbol);
  }
}
