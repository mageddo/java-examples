package com.example.fruit.mapper;

import com.example.fruit.domain.Fruit;
import com.example.fruit.dataformat.FruitReqV1;
import com.example.fruit.dataformat.FruitResV1;

public class FruitMapper {

  private FruitMapper() {
  }

  public static Fruit of(final FruitReqV1 req) {
    final var fruit = new Fruit();
    fruit.setId(req.id());
    fruit.setName(req.name());
    fruit.setColor(req.color());
    fruit.setSeason(req.season());
    return fruit;
  }

  public static FruitResV1 to(final Fruit fruit) {
    return new FruitResV1(
        fruit.getId(),
        fruit.getName(),
        fruit.getColor(),
        fruit.getSeason()
    );
  }
}
