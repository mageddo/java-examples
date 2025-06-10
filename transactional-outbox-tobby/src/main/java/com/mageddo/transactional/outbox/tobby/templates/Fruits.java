package com.mageddo.transactional.outbox.tobby.templates;

import com.mageddo.transactional.outbox.tobby.Fruit;

public class Fruits {
  public static Fruit orange() {
    return Fruit.builder()
        .name("Orange")
        .build();
  }

  public static Fruit grape() {
    return Fruit.builder()
        .name("Grape")
        .build();
  }

  public static Fruit apple() {
    return Fruit.builder()
        .name("Apple")
        .build();
  }
}
