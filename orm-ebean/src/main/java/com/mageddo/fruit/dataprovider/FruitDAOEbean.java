package com.mageddo.fruit.dataprovider;

import com.mageddo.fruit.domain.Fruit;
import com.mageddo.fruit.dataprovider.mapper.FruitMapper;
import io.ebean.Database;
import io.ebean.InsertOptions;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.UUID;

@Singleton
public class FruitDAOEbean implements FruitDAO {

  private final Database database;

  @Inject
  public FruitDAOEbean(final Database database) {
    this.database = database;
  }

  @Override
  public Fruit createIfAbsent(final Fruit fruit) {
    final var insertOptions = InsertOptions.builder()
        .onConflictNothing()
        .build();
    this.database.insert(FruitMapper.toRow(fruit), insertOptions);
    return this.find(fruit.id());
  }

  @Override
  public Fruit save(final Fruit fruit) {
    final var insertOptions = InsertOptions.builder()
        .onConflictUpdate()
        .build();
    this.database.insert(FruitMapper.toRow(fruit), insertOptions);
    return this.find(fruit.id());
  }

  @Override
  public Fruit find(final UUID id) {
    final var row = this.database.find(FruitRow.class, id);
    if (row == null) {
      return null;
    }
    return FruitMapper.toDomain(row);
  }
}
