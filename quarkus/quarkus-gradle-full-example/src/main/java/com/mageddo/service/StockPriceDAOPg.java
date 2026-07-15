package com.mageddo.service;

import java.util.List;

import jakarta.inject.Singleton;

import com.mageddo.domain.Stock;
import com.mageddo.rowmapper.StockRowMapper;

import org.jdbi.v3.core.Jdbi;

import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class StockPriceDAOPg implements StockPriceDAO {

  private final Jdbi jdbi;

  @Override
  public void update(Stock stock) {
    final var sql = """
        INSERT INTO STOCK VALUES (:symbol, :price)
        ON CONFLICT (IDT_STOCK) DO UPDATE
          SET NUM_PRICE = :price
        """;
    this.jdbi.useHandle(handle -> {
      handle
          .createUpdate(sql)
          .bind("symbol", stock.getSymbol())
          .bind("price", stock.getPrice())
          .execute()
      ;
    });
  }

  @Override
  public Stock find(String symbol) {
    final var sql = """
        SELECT *
        FROM STOCK
        WHERE IDT_STOCK = ?
        """;
    return this.jdbi.withHandle(h -> h
        .createQuery(sql)
        .bind(0, symbol)
        .map(new StockRowMapper())
        .one()
    );
  }

  @Override
  public boolean createIfAbsent(Stock stock) {
    return this.jdbi.withHandle(h -> h.execute("""
            INSERT INTO STOCK VALUES (?, ?) ON CONFLICT DO NOTHING
            """,
        stock.getSymbol(),
        stock.getPrice()
    )) == 1;
  }

  @Override
  public List<Stock> find() {
    return this.jdbi.withHandle(h -> h
        .createQuery("SELECT * FROM STOCK")
        .map(new StockRowMapper())
        .list()
    );
  }
}
