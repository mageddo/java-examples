package com.mageddo.main;

import org.junit.jupiter.api.Test;

import testing.utils.StubCtx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FruitDeliveryResourceTest {
  @Test
  void mustDelivery(){
    // arrange
    final var ctx = StubCtx.create();
    final var dao = ctx.fruitDao();
    final var resource = ctx.fruitResource();
    final var fruit = "Orange";

    // act
    resource.deliver(fruit);

    // assert
    final var fruits = dao.getDelivered();
    assertFalse(fruits.isEmpty());
    assertEquals("[Orange]", fruits.toString());
  }
}
