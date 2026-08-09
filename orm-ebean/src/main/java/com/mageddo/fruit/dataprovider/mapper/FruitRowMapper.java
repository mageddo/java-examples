package com.mageddo.fruit.dataprovider.mapper;

import com.mageddo.fruit.dataprovider.FruitRow;
import com.mageddo.fruit.Fruit;

public class FruitRowMapper {

  private FruitRowMapper() {
  }

  public static Fruit toDomain(FruitRow row) {
    return new Fruit(row.getId(), row.getName(), row.getColor(), row.getSeason());
  }

  public static FruitRow of(Fruit fruit) {
    final var row = new FruitRow();
    row.setId(fruit.getId());
    row.setName(fruit.getName());
    row.setColor(fruit.getColor());
    row.setSeason(fruit.getSeason());
    return row;
  }
}
