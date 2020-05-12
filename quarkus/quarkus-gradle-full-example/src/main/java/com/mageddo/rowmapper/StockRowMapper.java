package com.mageddo.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mageddo.domain.Stock;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class StockRowMapper implements RowMapper<Stock> {
  @Override
  public Stock map(ResultSet rs, StatementContext ctx) throws SQLException {
    return Stock
        .builder()
        .symbol(rs.getString("IDT_STOCK"))
        .price(rs.getBigDecimal("NUM_PRICE"))
        .build()
        ;
  }
}
