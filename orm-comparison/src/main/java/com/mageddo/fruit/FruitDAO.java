package com.mageddo.fruit;

import java.util.List;
import java.util.UUID;

public interface FruitDAO {

  boolean createIfAbsent(Fruit fruit);

  boolean save(Fruit fruit);

  Fruit find(UUID id);

  List<Fruit> findByName(String name);
}
