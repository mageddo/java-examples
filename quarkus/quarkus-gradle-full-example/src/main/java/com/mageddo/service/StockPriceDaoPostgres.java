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
    final StringBuilder sql = new StringBuilder()
        .append("INSERT INTO STOCK VALUES (:symbol, :price) \n")
        .append("ON CONFLICT (IDT_STOCK) DO UPDATE \n")
        .append("SET \n")
        .append("  NUM_PRICE = :price \n");
    jdbi.useHandle(handle -> {
      handle
          .createUpdate(sql.toString())
          .bind("symbol", stock.getSymbol())
          .bind("price", stock.getPrice())
          .execute()
      ;
    });
  }

  @Override
  public Stock getStock(String symbol) {
    return jdbi.withHandle(handle -> {
      return handle
          .createQuery("SELECT * FROM STOCK WHERE IDT_STOCK = ?")
          .bind(0, symbol)
          .map(new StockRowMapper())
          .one()
          ;
    });
  }

  @Override
  public void createStock(Stock stock) {
    try {
      this.jdbi.useHandle(handle -> {
        handle.execute("INSERT INTO STOCK VALUES (?, ?)", stock.getSymbol(), stock.getPrice());
      });
    } catch (StatementException e) {
      if (e.getMessage()
          .contains("stock_pkey")) {
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
