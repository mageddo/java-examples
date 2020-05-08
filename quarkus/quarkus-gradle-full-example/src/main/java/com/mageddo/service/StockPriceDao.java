package com.mageddo.service;

import com.mageddo.domain.Stock;

public interface StockPriceDao {

  void updateStockPrice(Stock stock);

  Stock getStock(String symbol);
}
