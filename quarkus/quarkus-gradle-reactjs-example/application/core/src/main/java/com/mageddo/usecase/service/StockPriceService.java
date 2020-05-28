package com.mageddo.usecase.service;

import java.util.List;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Singleton;
import javax.transactionv2.Propagation;
import javax.transactionv2.Transactional;

import com.mageddo.dataprovider.StockPriceDao;
import com.mageddo.usecase.domain.Stock;
import com.mageddo.usecase.exception.DuplicatedStockException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    stocks.forEach(it -> {
      try {
        self().createStockNested(it);
      } catch (DuplicatedStockException e) {
        log.info("status=already-exists, stock={}", it.getSymbol());
      }
    });
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
