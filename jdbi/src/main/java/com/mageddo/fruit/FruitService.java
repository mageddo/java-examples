package com.mageddo.fruit;

import java.util.List;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class FruitService {

  private final FruitDAO fruitDAO;

  @Transactional
  public void create(Fruit fruit) {
    this.fruitDAO.create(fruit);
  }

  public List<Fruit> find() {
    return this.fruitDAO.find();
  }
}
