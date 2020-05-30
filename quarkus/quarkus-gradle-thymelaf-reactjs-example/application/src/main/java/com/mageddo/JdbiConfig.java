package com.mageddo;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;

public class JdbiConfig {
  @Produces
  @Singleton
  public Jdbi jdbi(DataSource dataSource) {
    return Jdbi.create(dataSource);
  }

}
