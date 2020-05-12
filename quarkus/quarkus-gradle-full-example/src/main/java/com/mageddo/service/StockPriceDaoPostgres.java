package com.mageddo.service;

import java.util.List;

import javax.inject.Singleton;

import com.mageddo.domain.Stock;
import com.mageddo.exception.DuplicatedStockException;

import com.mageddo.rowmapper.StockRowMapper;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.StatementException;

import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class StockPriceDaoPostgres implements StockPriceDao {

  private final Jdbi jdbi;

  @Override
  public void updateStockPrice(Stock stock) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Stock getStock(String symbol) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void createStock(Stock stock) {
    try {
      this.jdbi.useHandle(handle -> {
        handle.execute("INSERT INTO STOCK VALUES (?, ?)", stock.getSymbol(), stock.getPrice());
      });
    } catch (StatementException e){
      if(e.getMessage().contains("stock_pkey")){
        throw new DuplicatedStockException(e);
      }
      throw e;
    }
  }

  @Override
  public List<Stock> find() {
    return this.jdbi.withHandle(handle -> {
      return handle
          .createQuery("SELECT * FROM STOCK")
          .map(new StockRowMapper())
          .list()
          ;
    });
  }
}
