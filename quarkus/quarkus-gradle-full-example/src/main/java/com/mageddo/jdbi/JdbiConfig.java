package com.mageddo.jdbi;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;

public class JdbiConfig {

  @Produces
  @Singleton
  public Jdbi jdbi(final DataSource dataSource) {
    return Jdbi.create(dataSource);
  }
}
