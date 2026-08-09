package com.mageddo.fruit.mapper;

import com.mageddo.fruit.df.FruitReqV1;
import com.mageddo.fruit.df.FruitResV1;
import com.mageddo.fruit.Fruit;
import com.mageddo.referrer.ReferrerMapper;
import java.time.Instant;

public class FruitMapper {

  private FruitMapper() {
  }

  public static Fruit of(FruitReqV1 req) {
    final var now = Instant.now();
    return Fruit.builder()
        .id(req.id())
        .name(req.name())
        .color(req.color())
        .season(req.season())
        .createdAt(now)
        .updatedAt(now)
        .referrer(ReferrerMapper.toDomain(req.referrer()))
        .build();
  }

  public static FruitResV1 toDf(Fruit fruit) {
    return new FruitResV1(
        fruit.getId(),
        fruit.getName(),
        fruit.getColor(),
        fruit.getSeason(),
        ReferrerMapper.toReq(fruit.getReferrer())
    );
  }
}
