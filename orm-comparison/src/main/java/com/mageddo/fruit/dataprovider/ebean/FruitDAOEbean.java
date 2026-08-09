package com.mageddo.fruit.dataprovider.ebean;

import com.mageddo.fruit.FruitDAO;
import com.mageddo.fruit.dataprovider.FruitRow;
import com.mageddo.fruit.Fruit;
import com.mageddo.fruit.dataprovider.mapper.FruitRowMapper;

import com.mageddo.fruit.config.ebean.EbeanInsertIfAbsent;

import io.ebean.Database;
import io.ebean.InsertOptions;

import jakarta.inject.Singleton;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class FruitDAOEbean implements FruitDAO {

  private final Database database;
  private final EbeanInsertIfAbsent insertIfAbsent;

  @Override
  public boolean createIfAbsent(Fruit fruit) {
    return this.insertIfAbsent.execute(this.database, FruitRowMapper.of(fruit));
  }

  @Override
  public boolean save(Fruit fruit) {
    final var insertOptions = InsertOptions
        .builder()
        .onConflictUpdate()
        .build();
    this.database.insert(FruitRowMapper.of(fruit), insertOptions);
    return false;
  }

  @Override
  public Fruit find(UUID id) {
    final var row = this.database.find(FruitRow.class, id);
    if (row == null) {
      return null;
    }
    return FruitRowMapper.toDomain(row);
  }

  @Override
  public List<Fruit> findByName(String name) {
    return this.database
        .findNative(
            FruitRow.class,
            """
                SELECT * FROM orm.FRUIT
                WHERE NAM_FRUIT = :name
                """
        )
        .setParameter("name", name)
        .findList()
        .stream()
        .map(FruitRowMapper::toDomain)
        .toList()
        ;
  }
}
