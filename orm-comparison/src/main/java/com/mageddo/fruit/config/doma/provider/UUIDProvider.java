package com.mageddo.fruit.config.doma.provider;

import java.util.UUID;

import com.mageddo.fruit.config.doma.type.UUIDType;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.JdbcTypeProvider;
import org.seasar.doma.jdbc.type.JdbcType;

@ExternalDomain
public class UUIDProvider extends JdbcTypeProvider<UUID> {

  private static final JdbcType<UUID> JDBC_TYPE = new UUIDType();

  @Override
  public JdbcType<UUID> getJdbcType() {
    return JDBC_TYPE;
  }
}
