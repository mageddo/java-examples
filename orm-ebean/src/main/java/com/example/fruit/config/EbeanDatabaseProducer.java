package com.example.fruit.config;

import com.example.fruit.domain.Fruit;
import io.ebean.Database;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class EbeanDatabaseProducer {

  private final Database database;

  @Inject
  public EbeanDatabaseProducer() {
    this.database = Database.builder()
      .name("db")
      .addClass(Fruit.class)
      .loadFromProperties()
      .build();
  }

  @Produces
  public Database database() {
    return this.database;
  }
}
