package com.mageddo.fruit.mapper;

import com.mageddo.fruit.df.FruitReqV1;
import com.mageddo.fruit.df.FruitResV1;
import com.mageddo.fruit.Fruit;
import com.mageddo.referrer.Referrer;
import com.mageddo.referrer.ReferrerReqV1;

public class FruitMapper {

  private FruitMapper() {
  }

  public static Fruit of(FruitReqV1 req) {
    return Fruit.builder()
        .id(req.id())
        .name(req.name())
        .color(req.color())
        .season(req.season())
        .referrer(toDomainReferrer(req.referrer()))
        .build();
  }

  public static FruitResV1 toDf(Fruit fruit) {
    return new FruitResV1(
        fruit.getId(),
        fruit.getName(),
        fruit.getColor(),
        fruit.getSeason(),
        toDfReferrer(fruit.getReferrer())
    );
  }

  static Referrer toDomainReferrer(ReferrerReqV1 referrer) {
    if (referrer == null) {
      return null;
    }
    return Referrer.builder()
        .id(referrer.id())
        .type(referrer.type())
        .build();
  }

  static ReferrerReqV1 toDfReferrer(Referrer referrer) {
    if (referrer == null) {
      return null;
    }
    return new ReferrerReqV1(referrer.getId(), referrer.getType());
  }
}
