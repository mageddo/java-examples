package com.mageddo.fruit;

import java.util.List;

public interface FruitDAO {

  void create(Fruit fruit);

  List<Fruit> find();
}
