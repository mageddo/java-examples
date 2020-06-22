package com.mageddo.dagger;

public class App {
  public static void main(String[] args) {
    final FruitDeliveryResource fruitResource = DaggerAppConfig
        .create()
        .fruitResource();
    fruitResource.deliver("Strawberry");
  }
}
