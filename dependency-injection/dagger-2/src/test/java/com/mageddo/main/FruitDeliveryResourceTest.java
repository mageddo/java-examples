package com.mageddo.main;

import com.mageddo.main.config.Ctx;

import org.junit.jupiter.api.Test;

class FruitDeliveryResourceTest {
  @Test
  void mustDelivery(){
    // arrange
    final var ctx = Ctx.create();
    final var resource = ctx.fruitResource();
    final var fruit = "Orange";

    // act
    resource.deliver(fruit);

    // assert
  }
}
