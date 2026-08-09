package com.mageddo.fruit.domain.templates;

import java.util.UUID;
import com.mageddo.fruit.Fruit;
import com.mageddo.referrer.Referrer;

public class FruitTemplates {

  public static final UUID BANANA_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
  public static final UUID GREEN_BANANA_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
  public static final String BANANA_REFERRER_ID = "8f1e6a9a-79a7-4d6a-8e56-e8f2a9d6ce0f";
  public static final String GREEN_BANANA_REFERRER_ID = "2f7a0f3e-4bb4-4ea8-9d3d-e6e9f3f4dfb0";
  public static final String REFERRER_ID = "USER";

  public static Fruit banana() {
    return Fruit.builder()
        .id(BANANA_ID)
        .name("Banana")
        .color("Yellow")
        .season("Summer")
        .build();
  }

  public static Fruit updatedBanana() {
    return Fruit.builder()
        .id(BANANA_ID)
        .name("Banana")
        .color("Green")
        .season("Summer")
        .build();
  }

  public static Fruit greenBanana() {
    return Fruit.builder()
        .id(GREEN_BANANA_ID)
        .name("Banana")
        .color("Green")
        .season("Summer")
        .build();
  }

  public static Fruit greenBananaAltSeason() {
    return Fruit.builder()
        .id(GREEN_BANANA_ID)
        .name("Green Banana")
        .color("Green")
        .season("Autumn")
        .build();
  }

  public static Fruit bananaWithReferrer() {
    return Fruit.builder()
        .id(BANANA_ID)
        .name("Banana")
        .color("Yellow")
        .season("Summer")
        .referrer(Referrer.builder()
            .id(BANANA_REFERRER_ID)
            .type(REFERRER_ID)
            .build())
        .build();
  }

  public static Fruit greenBananaWithReferrer() {
    return Fruit.builder()
        .id(GREEN_BANANA_ID)
        .name("Banana")
        .color("Green")
        .season("Summer")
        .referrer(Referrer.builder()
            .id(GREEN_BANANA_REFERRER_ID)
            .type(REFERRER_ID)
            .build())
        .build();
  }

  public static Fruit greenBananaWithReferrerUpdated() {
    return Fruit.builder()
        .id(GREEN_BANANA_ID)
        .name("Green Banana")
        .color("Green")
        .season("Autumn")
        .referrer(Referrer.builder()
            .id(GREEN_BANANA_REFERRER_ID)
            .type("SOCIAL")
            .build())
        .build();
  }
}
