package com.mageddo.fruit.config.doma.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.seasar.doma.jdbc.type.AbstractJdbcType;

public class InstantType extends AbstractJdbcType<Instant> {

  public InstantType() {
    super(Types.TIMESTAMP);
  }

  @Override
  protected Instant doGetValue(
      ResultSet rs,
      int index
  ) throws SQLException {
    final var value = rs.getObject(index, OffsetDateTime.class);
    if (value == null) {
      return null;
    }
    return value.toInstant();
  }

  @Override
  protected void doSetValue(
      PreparedStatement statement,
      int index,
      Instant value
  ) throws SQLException {
    statement.setObject(index, value == null ? null : value.atOffset(ZoneOffset.UTC));
  }

  @Override
  protected Instant doGetValue(
      CallableStatement statement,
      int index
  ) throws SQLException {
    final var value = statement.getObject(index, OffsetDateTime.class);
    if (value == null) {
      return null;
    }
    return value.toInstant();
  }

  @Override
  protected String doConvertToLogFormat(Instant value) {
    if (value == null) {
      return null;
    }
    return value.toString();
  }
}
