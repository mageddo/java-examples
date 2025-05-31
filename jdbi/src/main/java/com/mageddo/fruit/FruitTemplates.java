package com.mageddo.fruit;

import java.util.UUID;

public class FruitTemplates {
  public static Fruit orange() {
    final var fruit = new Fruit();
    fruit.setId(UUID.fromString("0f93a79f-2455-4bfb-8047-b801fcf87fcc"));
    fruit.setName("Orange");
    return fruit;
  }
}
