package com.mageddo.fruit.dataprovider;

import com.mageddo.fruit.domain.Fruit;
import java.util.UUID;

public interface FruitDAO {

  Fruit createIfAbsent(final Fruit fruit);

  Fruit save(final Fruit fruit);

  Fruit find(final UUID id);
}
