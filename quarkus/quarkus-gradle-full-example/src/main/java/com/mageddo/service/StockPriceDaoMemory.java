package com.mageddo.service;

import com.mageddo.domain.Stock;

import javax.enterprise.context.ApplicationScoped;

import java.util.LinkedHashMap;
import java.util.Map;

@ApplicationScoped
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
