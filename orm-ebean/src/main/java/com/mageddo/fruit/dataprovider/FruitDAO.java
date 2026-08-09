package com.mageddo.fruit.dataprovider;

import com.mageddo.fruit.Fruit;
import java.util.UUID;

public interface FruitDAO {

  boolean createIfAbsent(Fruit fruit);

  Fruit save(Fruit fruit);

  Fruit find(UUID id);
}
