package com.mageddo.fruit.dataprovider.mapper;

import com.mageddo.fruit.dataprovider.FruitRow;
import com.mageddo.fruit.Fruit;
import com.mageddo.referrer.ReferrerMapper;

public class FruitRowMapper {

  private FruitRowMapper() {
  }

  public static Fruit toDomain(FruitRow row) {
    return Fruit.builder()
        .id(row.getId())
        .name(row.getName())
        .color(row.getColor())
        .season(row.getSeason())
        .referrer(ReferrerMapper.toDomain(row.getReferrer()))
        .build();
  }

  public static FruitRow of(Fruit fruit) {
    final var row = new FruitRow();
    row.setId(fruit.getId());
    row.setName(fruit.getName());
    row.setColor(fruit.getColor());
    row.setSeason(fruit.getSeason());
    row.setReferrer(ReferrerMapper.toRow(fruit.getReferrer()));
    return row;
  }
}
