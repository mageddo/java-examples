package com.mageddo;

import javax.sql.DataSource;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

import org.jdbi.v3.core.Jdbi;

public class JdbiConfig {
  @Produces
  @Singleton
  public Jdbi jdbi(DataSource dataSource) {
    return Jdbi.create(dataSource);
  }
}
