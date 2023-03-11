package com.mageddo.main;

import com.mageddo.main.config.Ctx;

public class App {
  public static void main(String[] args) {
    final FruitDeliveryResource fruitResource = Ctx
        .create()
        .fruitResource();
    fruitResource.deliver("Strawberry");
  }
}
