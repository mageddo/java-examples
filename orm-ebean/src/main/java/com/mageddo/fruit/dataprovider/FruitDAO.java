package com.mageddo.fruit.dataprovider;

import com.mageddo.fruit.domain.Fruit;
import java.util.UUID;

public interface FruitDAO {

  boolean createIfAbsent(Fruit fruit);

  Fruit save(Fruit fruit);

  Fruit find(UUID id);
}
