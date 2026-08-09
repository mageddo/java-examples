package com.mageddo.fruit.config.doma;

import jakarta.inject.Singleton;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.QueryDsl;

public final class DomaConfig {

  private DomaConfig() {
  }

  @Singleton
  public QueryDsl queryDsl(Config config) {
    return new QueryDsl(config);
  }
}

