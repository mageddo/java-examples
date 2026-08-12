package com.mageddo.basket.templates;

import java.time.Instant;
import java.util.UUID;

import com.mageddo.basket.Basket;

public final class BasketTemplates {

  public static final UUID FRUIT_BASKET_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

  private BasketTemplates() {
  }

  public static Basket fruitBasket() {
    final var now = Instant.now();
    return Basket
        .builder()
        .id(FRUIT_BASKET_ID)
        .name("Fruit Basket")
        .createdAt(now)
        .updatedAt(now)
        .build();
  }

  public static Basket fruitBasketUpdated() {
    final var now = Instant.now();
    return Basket
        .builder()
        .id(FRUIT_BASKET_ID)
        .name("Tropical Basket")
        .createdAt(now)
        .updatedAt(now)
        .build();
  }
}
