package com.mageddo.fruit.domain.templates;

import java.util.UUID;
import com.mageddo.fruit.Fruit;

public class FruitTemplates {

  public static final UUID BANANA_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
  public static final UUID GREEN_BANANA_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

  public static Fruit banana() {
    return new Fruit(BANANA_ID, "Banana", "Yellow", "Summer");
  }

  public static Fruit updatedBanana() {
    return new Fruit(BANANA_ID, "Banana", "Green", "Summer");
  }

  public static Fruit greenBanana() {
    return new Fruit(GREEN_BANANA_ID, "Banana", "Green", "Summer");
  }

  public static Fruit greenBananaAltSeason() {
    return new Fruit(GREEN_BANANA_ID, "Green Banana", "Green", "Autumn");
  }
}
