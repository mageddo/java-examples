package com.mageddo.fruit.config.doma.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

import org.seasar.doma.jdbc.type.AbstractJdbcType;

public class UUIDType extends AbstractJdbcType<UUID> {

  public UUIDType() {
    super(Types.OTHER);
  }

  @Override
  protected UUID doGetValue(ResultSet rs, int index) throws SQLException {
    return rs.getObject(index, UUID.class);
  }

  @Override
  protected void doSetValue(
      PreparedStatement statement,
      int index,
      UUID value
  ) throws SQLException {
    statement.setObject(index, value, Types.OTHER);
  }

  @Override
  protected UUID doGetValue(
      CallableStatement statement,
      int index
  ) throws SQLException {
    return statement.getObject(index, UUID.class);
  }

  @Override
  protected String doConvertToLogFormat(UUID value) {
    if (value == null) {
      return null;
    }
    return value.toString();
  }
}
