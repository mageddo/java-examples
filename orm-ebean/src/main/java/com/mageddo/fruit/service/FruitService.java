package com.mageddo.fruit.service;

import com.mageddo.fruit.dataprovider.FruitDAO;
import com.mageddo.fruit.domain.Fruit;
import jakarta.inject.Singleton;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class FruitService {

  private final FruitDAO fruitDao;

  public Fruit createIfAbsent(Fruit fruit) {
    return this.fruitDao.createIfAbsent(fruit);
  }

  public Fruit save(Fruit fruit) {
    return this.fruitDao.save(fruit);
  }

  public Fruit find(UUID id) {
    return this.fruitDao.find(id);
  }
}
