package com.mageddo.fruit.templates;

import java.time.Instant;
import java.util.UUID;
import com.mageddo.fruit.Fruit;
import com.mageddo.referrer.tempaltes.ReferrerTemplates;

public class FruitTemplates {

  public static final UUID BANANA_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
  public static final UUID GREEN_BANANA_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
  public static Fruit banana() {
    final var now = Instant.now();
    return Fruit.builder()
        .id(BANANA_ID)
        .name("Banana")
        .color("Yellow")
        .season("Summer")
        .createdAt(now)
        .updatedAt(now)
        .build();
  }

  public static Fruit updatedBanana() {
    final var now = Instant.now();
    return Fruit.builder()
        .id(BANANA_ID)
        .name("Banana")
        .color("Green")
        .season("Summer")
        .createdAt(now)
        .updatedAt(now)
        .build();
  }

  public static Fruit greenBanana() {
    final var now = Instant.now();
    return Fruit.builder()
        .id(GREEN_BANANA_ID)
        .name("Banana")
        .color("Green")
        .season("Summer")
        .createdAt(now)
        .updatedAt(now)
        .build();
  }

  public static Fruit greenBananaAltSeason() {
    final var now = Instant.now();
    return Fruit.builder()
        .id(GREEN_BANANA_ID)
        .name("Green Banana")
        .color("Green")
        .season("Autumn")
        .createdAt(now)
        .updatedAt(now)
        .build();
  }

  public static Fruit bananaWithReferrer() {
    final var now = Instant.now();
    return Fruit.builder()
        .id(BANANA_ID)
        .name("Banana")
        .color("Yellow")
        .season("Summer")
        .referrer(ReferrerTemplates.bananaReferrer())
        .createdAt(now)
        .updatedAt(now)
        .build();
  }

  public static Fruit greenBananaWithReferrer() {
    final var now = Instant.now();
    return Fruit.builder()
        .id(GREEN_BANANA_ID)
        .name("Banana")
        .color("Green")
        .season("Summer")
        .referrer(ReferrerTemplates.greenBananaReferrer())
        .createdAt(now)
        .updatedAt(now)
        .build();
  }

  public static Fruit greenBananaWithReferrerUpdated() {
    final var now = Instant.now();
    return Fruit.builder()
        .id(GREEN_BANANA_ID)
        .name("Green Banana")
        .color("Green")
        .season("Autumn")
        .referrer(ReferrerTemplates.greenBananaReferrerUpdated())
        .createdAt(now)
        .updatedAt(now)
        .build();
  }
}
