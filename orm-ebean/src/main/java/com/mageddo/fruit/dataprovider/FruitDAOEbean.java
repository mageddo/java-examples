package com.mageddo.fruit.dataprovider;

import com.mageddo.fruit.domain.Fruit;
import com.mageddo.fruit.dataprovider.mapper.FruitMapper;
import io.ebean.Database;
import io.ebean.InsertOptions;
import jakarta.inject.Singleton;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class FruitDAOEbean implements FruitDAO {

  private final Database database;

  @Override
  public Fruit createIfAbsent(Fruit fruit) {
    final var insertOptions = InsertOptions.builder()
        .onConflictNothing()
        .build();
    this.database.insert(FruitMapper.toRow(fruit), insertOptions);
    return this.find(fruit.getId());
  }

  @Override
  public Fruit save(Fruit fruit) {
    final var insertOptions = InsertOptions.builder()
        .onConflictUpdate()
        .build();
    this.database.insert(FruitMapper.toRow(fruit), insertOptions);
    return this.find(fruit.getId());
  }

  @Override
  public Fruit find(UUID id) {
    final var row = this.database.find(FruitRow.class, id);
    if (row == null) {
      return null;
    }
    return FruitMapper.toDomain(row);
  }
}
