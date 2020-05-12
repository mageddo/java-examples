package com.mageddo.service;

import java.util.List;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Singleton;

import com.mageddo.domain.Stock;
import com.mageddo.jdbi.Propagation;
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

  @Transactional
  public void createStockNested(List<Stock> stocks) {
    stocks.forEach(it -> self().createStockNested(it));
  }

  @Transactional(propagation = Propagation.NESTED)
  public void createStockNested(Stock stock) {
    this.stockPriceDao.createStock(stock);
  }

  StockPriceService self(){
    return CDI.current()
        .select(StockPriceService.class)
        .get();
  }
}
