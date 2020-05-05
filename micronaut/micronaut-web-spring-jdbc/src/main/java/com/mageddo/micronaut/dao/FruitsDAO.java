package com.mageddo.micronaut.dao;


import java.util.List;

import com.mageddo.micronaut.entity.FruitEntity;

public interface FruitsDAO {
  void traceSelect();

  List<FruitEntity> getFruits();

  int countTraces();
}
