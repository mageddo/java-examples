package com.mageddo.fruit.config;

import io.ebean.Database;
import com.mageddo.fruit.dataprovider.FruitRow;
import jakarta.inject.Singleton;
import jakarta.enterprise.inject.Produces;

@Singleton
public class EbeanDatabaseProducer {

  private final Database database;

  public EbeanDatabaseProducer() {
    this.database = Database.builder()
      .name("db")
      .addClass(FruitRow.class)
      .loadFromProperties()
      .build();
  }

  @Produces
  public Database database() {
    return this.database;
  }
}
