package com.mageddo.service;

import java.util.List;

import com.mageddo.domain.Stock;

public interface StockPriceDAO {

  void update(Stock stock);

  Stock find(String symbol);

  boolean createIfAbsent(Stock stock);

  List<Stock> find();
}
