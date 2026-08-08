package com.example.fruit.service;

import com.example.fruit.domain.Fruit;
import io.ebean.Database;
import io.ebean.InsertOptions;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FruitService {

  private final Database database;

  public FruitService(Database database) {
    this.database = database;
  }

  public Fruit createIfAbsent(final Fruit fruit) {
    validateId(fruit.getId());

    final var insertOptions = InsertOptions.builder()
      .onConflictNothing()
      .build();

    this.database.insert(fruit, insertOptions);

    return this.database.find(Fruit.class, fruit.getId());
  }

  public Fruit upsert(final Fruit fruit) {
    validateId(fruit.getId());

    final var insertOptions = InsertOptions.builder()
      .onConflictUpdate()
      .build();

    this.database.insert(fruit, insertOptions);

    return this.database.find(Fruit.class, fruit.getId());
  }

  public Fruit find(final Long id) {
    return this.database.find(Fruit.class, id);
  }

  private void validateId(final Long id) {
    if (id == null) {
      throw new IllegalArgumentException("Fruit id is required");
    }
  }
}
