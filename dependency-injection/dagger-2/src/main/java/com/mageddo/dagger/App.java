package com.mageddo.dagger;

public class App {
  public static void main(String[] args) {
    final var fruitResource = DaggerAppConfig
        .create()
        .fruitResource();
    fruitResource.deliver("Strawberry");
  }
}
