package com.mageddo.dataprovider;

import java.util.List;

import com.mageddo.usecase.domain.Stock;

public interface StockPriceDao {

  void updateStockPrice(Stock stock);

  Stock getStock(String symbol);

  void createStock(Stock stock);

  List<Stock> find();
}
