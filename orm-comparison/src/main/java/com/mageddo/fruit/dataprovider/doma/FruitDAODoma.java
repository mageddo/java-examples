package com.mageddo.fruit.dataprovider.doma;

import com.mageddo.fruit.Fruit;
import com.mageddo.fruit.FruitDAO;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Singleton
@Named("doma")
@RequiredArgsConstructor
public class FruitDAODoma implements FruitDAO {

  private final FruitDomaDao dao;

  @Override
  public boolean createIfAbsent(Fruit fruit) {
    final var existing = this.find(fruit.getId());
    if (existing != null) {
      return false;
    }
    this.dao.insert(FruitDomaMapper.toRow(fruit));
    return true;
  }

  @Override
  public Fruit save(Fruit fruit) {
    final var row = FruitDomaMapper.toRow(fruit);
    final var updated = this.dao.update(row);

    if (updated == 0) {
      this.dao.insert(row);
    }

    return this.find(fruit.getId());
  }

  @Override
  public Fruit find(UUID id) {
    return FruitDomaMapper.toDomain(this.dao.findById(id));
  }
}

