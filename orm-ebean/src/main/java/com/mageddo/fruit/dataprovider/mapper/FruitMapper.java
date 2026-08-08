package com.mageddo.fruit.dataprovider.mapper;

import com.mageddo.fruit.dataprovider.FruitRow;
import com.mageddo.fruit.domain.Fruit;
import com.mageddo.fruit.dataformat.FruitReqV1;
import com.mageddo.fruit.dataformat.FruitResV1;

public class FruitMapper {

  private FruitMapper() {
  }

  public static Fruit of(final FruitReqV1 req) {
    return new Fruit(
        req.id(),
        req.name(),
        req.color(),
        req.season()
    );
  }

  public static FruitResV1 to(final Fruit fruit) {
    return new FruitResV1(
        fruit.id(),
        fruit.name(),
        fruit.color(),
        fruit.season()
    );
  }

  public static Fruit toDomain(final FruitRow row) {
    return new Fruit(
        row.getId(),
        row.getName(),
        row.getColor(),
        row.getSeason()
    );
  }

  public static FruitRow toRow(final Fruit fruit) {
    final var row = new FruitRow();
    row.setId(fruit.id());
    row.setName(fruit.name());
    row.setColor(fruit.color());
    row.setSeason(fruit.season());
    return row;
  }
}
