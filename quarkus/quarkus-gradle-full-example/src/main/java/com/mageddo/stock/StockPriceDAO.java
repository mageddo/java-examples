package com.mageddo.stock;

import java.util.List;

public interface StockPriceDAO {

  void update(Stock stock);

  Stock find(String symbol);

  boolean createIfAbsent(Stock stock);

  List<Stock> find();
}
