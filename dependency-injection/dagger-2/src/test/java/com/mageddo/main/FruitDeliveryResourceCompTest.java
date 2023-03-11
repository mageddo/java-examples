package com.mageddo.main;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testing.utils.StubCtx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FruitDeliveryResourceCompTest {

  static StubCtx ctx;

  FruitDAOMock fruitDAO;

  @BeforeAll
  static void beforeAll(){
    ctx = StubCtx.create();
  }

  @BeforeEach
  void beforeEach(){
    this.fruitDAO = ctx.fruitDao();
    this.fruitDAO.clear();
  }

  @Test
  void mustDelivery(){
    // arrange

    final var resource = ctx.fruitResource();
    final var fruit = "Orange";

    // act
    resource.deliver(fruit);

    // assert
    final var fruits = this.fruitDAO.getDelivered();
    assertFalse(fruits.isEmpty());
    assertEquals("[Orange]", fruits.toString());
  }

  @Test
  void mustDeliveryTwoFruits(){
    // arrange
    final var resource = ctx.fruitResource();
    final var orange = "Orange";
    final var grape = "Grape";

    // act
    resource.deliver(orange);
    resource.deliver(grape);

    // assert
    final var fruits = this.fruitDAO.getDelivered();
    assertFalse(fruits.isEmpty());
    assertEquals("[Orange, Grape]", fruits.toString());
  }
}
