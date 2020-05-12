package com.mageddo.service;

import java.util.List;

import com.mageddo.domain.Stock;

public interface StockPriceDao {

  void updateStockPrice(Stock stock);

  Stock getStock(String symbol);

  void createStock(Stock stock);

  List<Stock> find();
}
