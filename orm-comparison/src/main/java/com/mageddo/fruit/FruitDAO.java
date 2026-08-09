package com.mageddo.fruit;

import java.util.UUID;

public interface FruitDAO {

  boolean createIfAbsent(Fruit fruit);

  boolean save(Fruit fruit);

  Fruit find(UUID id);
}
