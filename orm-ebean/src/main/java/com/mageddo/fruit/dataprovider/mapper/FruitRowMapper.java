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
    return new Fruit(
        row.getId(),
        row.getName(),
        row.getColor(),
        row.getSeason(),
        toDomainReferrer(row.getReferrer())
    );
  }

  static Referrer toDomainReferrer(ReferrerRow row) {
    if (row == null) {
      return null;
    }
    if (StringUtils.isAllBlank(row.getId(), row.getType())) {
      return null;
    }
    return new Referrer(row.getId(), row.getType());
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
