package com.mageddo.testing;

import io.ebean.Database;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class DatabaseConfigurator {

  private final Database database;

  public void truncate() {
    this.database.createSqlUpdate("truncate table ebean_orm.fruit").execute();
  }
}
