package com.mageddo.fruit.dataprovider.mapper;

import com.mageddo.fruit.dataprovider.FruitRow;
import com.mageddo.fruit.Fruit;
import com.mageddo.referrer.Referrer;
import com.mageddo.referrer.ReferrerRow;
import org.apache.commons.lang3.StringUtils;

public class FruitRowMapper {

  private FruitRowMapper() {
  }

  public static Fruit toDomain(FruitRow row) {
    return Fruit.builder()
        .id(row.getId())
        .name(row.getName())
        .color(row.getColor())
        .season(row.getSeason())
        .referrer(toDomainReferrer(row.getReferrer()))
        .build();
  }

  static Referrer toDomainReferrer(ReferrerRow row) {
    if (row == null) {
      return null;
    }
    if (StringUtils.isAllBlank(row.getId(), row.getType())) {
      return null;
    }
    return Referrer.builder()
        .id(row.getId())
        .type(row.getType())
        .build();
  }

  public static FruitRow of(Fruit fruit) {
    final var row = new FruitRow();
    row.setId(fruit.getId());
    row.setName(fruit.getName());
    row.setColor(fruit.getColor());
    row.setSeason(fruit.getSeason());
    row.setReferrer(toRowReferrer(fruit.getReferrer()));
    return row;
  }

  static ReferrerRow toRowReferrer(Referrer referrer) {
    if (referrer == null || StringUtils.isAllBlank(referrer.getId(), referrer.getType())) {
      return null;
    }
    final var row = new ReferrerRow();
    row.setId(referrer.getId());
    row.setType(referrer.getType());
    return row;
  }
}
