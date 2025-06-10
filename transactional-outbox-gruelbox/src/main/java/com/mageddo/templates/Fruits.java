package com.mageddo.templates;

import com.mageddo.Fruit;

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
