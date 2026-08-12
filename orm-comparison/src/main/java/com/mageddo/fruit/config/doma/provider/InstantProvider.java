package com.mageddo.fruit.config.doma.provider;

import java.time.Instant;

import com.mageddo.fruit.config.doma.type.InstantType;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.JdbcTypeProvider;
import org.seasar.doma.jdbc.type.JdbcType;

@ExternalDomain
public class InstantProvider extends JdbcTypeProvider<Instant> {

  private static final JdbcType<Instant> JDBC_TYPE = new InstantType();

  @Override
  public JdbcType<Instant> getJdbcType() {
    return JDBC_TYPE;
  }
}
