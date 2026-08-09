package com.mageddo.fruit.dataprovider.doma;

import com.mageddo.fruit.Fruit;
import com.mageddo.referrer.Referrer;
import com.mageddo.referrer.dataprovider.doma.ReferrerRow;

import com.mageddo.referrer.dataprovider.doma.ReferrerRowMapper;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;

public class FruitDomaMapper {

  private FruitDomaMapper() {
  }

  public static Fruit toDomain(FruitDomaRow row) {
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

  public static FruitDomaRow toRow(Fruit fruit) {
    final var row = new FruitDomaRow();
    row.setId(fruit.getId());
    row.setName(fruit.getName());
    row.setColor(fruit.getColor());
    row.setSeason(fruit.getSeason());
    row.setCreatedAt(fruit.getCreatedAt());
    row.setUpdatedAt(fruit.getUpdatedAt());
    row.setReferrer(ReferrerRowMapper.of(fruit.getReferrer()));
    return row;
  }

}
