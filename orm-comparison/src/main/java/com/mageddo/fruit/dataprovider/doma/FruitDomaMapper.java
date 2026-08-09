package com.mageddo.fruit.dataprovider.doma;

import com.mageddo.fruit.Fruit;
import com.mageddo.referrer.Referrer;
import org.apache.commons.lang3.StringUtils;

public class FruitDomaMapper {

  private FruitDomaMapper() {
  }

  public static Fruit toDomain(FruitDomaRow row) {
    if (row == null) {
      return null;
    }
    return Fruit.builder()
        .id(row.getId())
        .name(row.getName())
        .color(row.getColor())
        .season(row.getSeason())
        .createdAt(row.getCreatedAt())
        .updatedAt(row.getUpdatedAt())
        .referrer(toDomain(row.getReferrer()))
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
    row.setReferrer(toRow(fruit.getReferrer()));
    return row;
  }

  static Referrer toDomain(FruitDomaReferrerRow row) {
    if (row == null || StringUtils.isAllBlank(row.getId(), row.getType())) {
      return null;
    }
    return Referrer.builder()
        .id(row.getId())
        .type(row.getType())
        .build();
  }

  static FruitDomaReferrerRow toRow(Referrer referrer) {
    if (referrer == null || StringUtils.isAllBlank(referrer.getId(), referrer.getType())) {
      return null;
    }
    final var row = new FruitDomaReferrerRow();
    row.setId(referrer.getId());
    row.setType(referrer.getType());
    return row;
  }
}
