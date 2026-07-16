package com.mageddo;

import javax.sql.DataSource;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.enums.EnumStrategy;
import org.jdbi.v3.core.enums.Enums;

public class JdbiConfig {

  @Produces
  @Singleton
  public Jdbi jdbi(DataSource dataSource) {
    final var jdbi = Jdbi.create(dataSource);
    jdbi
        .getConfig(Enums.class)
        .setEnumStrategy(EnumStrategy.BY_NAME)
    ;
    return jdbi;
  }

}
