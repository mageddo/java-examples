package com.mageddo.fruit.dataprovider.doma;

import com.mageddo.fruit.Fruit;

import com.mageddo.referrer.dataprovider.doma.ReferrerRowMapper;

import java.util.List;

public class FruitRowMapper {

  private FruitRowMapper() {
  }

  public static Fruit toDomain(FruitRow row) {
    if (row == null) {
      return null;
    }
    return Fruit
        .builder()
        .id(row.getId())
        .name(row.getName())
        .color(row.getColor())
        .season(row.getSeason())
        .createdAt(row.getCreatedAt())
        .updatedAt(row.getUpdatedAt())
        .referrer(ReferrerRowMapper.toDomain(row.getReferrer()))
        .build();
  }

  public static FruitRow toRow(Fruit fruit) {
    final var row = new FruitRow();
    row.setId(fruit.getId());
    row.setName(fruit.getName());
    row.setColor(fruit.getColor());
    row.setSeason(fruit.getSeason());
    row.setCreatedAt(fruit.getCreatedAt());
    row.setUpdatedAt(fruit.getUpdatedAt());
    row.setReferrer(ReferrerRowMapper.of(fruit.getReferrer()));
    return row;
  }

  public static List<Fruit> toDomain(List<FruitRow> rows) {
    return rows
        .stream()
        .map(FruitRowMapper::toDomain)
        .toList();
  }
}
