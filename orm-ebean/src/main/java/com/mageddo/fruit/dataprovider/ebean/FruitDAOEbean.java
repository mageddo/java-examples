package com.mageddo.fruit.dataprovider.ebean;

import com.mageddo.fruit.FruitDAO;
import com.mageddo.fruit.dataprovider.FruitRow;
import com.mageddo.fruit.Fruit;
import com.mageddo.fruit.dataprovider.mapper.FruitRowMapper;
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
  public boolean createIfAbsent(Fruit fruit) {
    final var exists = this.find(fruit.getId());
    if (exists != null) {
      return false;
    }
    this.database.insert(FruitRowMapper.of(fruit));
    return true;
  }

  @Override
  public Fruit save(Fruit fruit) {
    final var insertOptions = InsertOptions.builder()
        .onConflictUpdate()
        .build();
    this.database.insert(FruitRowMapper.of(fruit), insertOptions);
    return this.find(fruit.getId());
  }

  @Override
  public Fruit find(UUID id) {
    final var row = this.database.find(FruitRow.class, id);
    if (row == null) {
      return null;
    }
    return FruitRowMapper.toDomain(row);
  }
}
