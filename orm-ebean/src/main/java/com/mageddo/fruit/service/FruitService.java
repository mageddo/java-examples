package com.mageddo.fruit.service;

import com.mageddo.fruit.dataprovider.FruitDAO;
import com.mageddo.fruit.domain.Fruit;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.UUID;

@Singleton
public class FruitService {

  private final FruitDAO fruitDao;

  @Inject
  public FruitService(final FruitDAO fruitDao) {
    this.fruitDao = fruitDao;
  }

  public Fruit createIfAbsent(final Fruit fruit) {
    return this.fruitDao.createIfAbsent(fruit);
  }

  public Fruit save(final Fruit fruit) {
    return this.fruitDao.save(fruit);
  }

  public Fruit find(final UUID id) {
    return this.fruitDao.find(id);
  }
}
