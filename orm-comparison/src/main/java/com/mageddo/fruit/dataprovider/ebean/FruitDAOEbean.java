package com.mageddo.fruit.dataprovider.ebean;

import com.mageddo.fruit.FruitDAO;
import com.mageddo.fruit.dataprovider.FruitRow;
import com.mageddo.fruit.Fruit;
import com.mageddo.fruit.dataprovider.mapper.FruitRowMapper;

import com.mageddo.persistence.GenericDAO;

import io.ebean.Database;

import jakarta.inject.Singleton;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class FruitDAOEbean implements FruitDAO {

  private final Database database;
  private final GenericDAO<FruitRow> genericDAO;

  @Override
  public boolean createIfAbsent(Fruit fruit) {
    return this.genericDAO.createIfAbsent(FruitRowMapper.of(fruit));
  }

  @Override
  public void save(Fruit fruit) {
    this.genericDAO.save(FruitRowMapper.of(fruit));
  }

  @Override
  public Fruit find(UUID id) {
    final var row = this.genericDAO.mustFind(id, FruitRow.class);
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
