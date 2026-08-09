package com.mageddo.fruit.dataprovider.doma;

import com.mageddo.fruit.Fruit;
import com.mageddo.referrer.Referrer;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;

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
        .createdAt(toInstant(row.getCreatedAt()))
        .updatedAt(toInstant(row.getUpdatedAt()))
        .referrer(toDomain(row.getReferrer()))
        .build();
  }

  public static FruitDomaRow toRow(Fruit fruit) {
    final var row = new FruitDomaRow();
    row.setId(fruit.getId().toString());
    row.setName(fruit.getName());
    row.setColor(fruit.getColor());
    row.setSeason(fruit.getSeason());
    row.setCreatedAt(toTimestamp(fruit.getCreatedAt()));
    row.setUpdatedAt(toTimestamp(fruit.getUpdatedAt()));
    row.setReferrer(toRow(fruit.getReferrer()));
    return row;
  }

  static Timestamp toTimestamp(java.time.Instant instant) {
    if (instant == null) {
      return null;
    }
    return Timestamp.from(instant);
  }

  static java.time.Instant toInstant(Timestamp timestamp) {
    if (timestamp == null) {
      return null;
    }
    return timestamp.toInstant();
  }

  static Referrer toDomain(FruitDomaReferrerRow row) {
    if (row == null || StringUtils.isAllBlank(row.id(), row.type())) {
      return null;
    }
    return Referrer.builder()
        .id(row.id())
        .type(row.type())
        .build();
  }

  static FruitDomaReferrerRow toRow(Referrer referrer) {
    if (referrer == null || StringUtils.isAllBlank(referrer.getId(), referrer.getType())) {
      return null;
    }
    return new FruitDomaReferrerRow(referrer.getId(), referrer.getType());
  }
}
