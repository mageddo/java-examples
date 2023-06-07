package com.mageddo.java11;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocalVariableForLambdasTest {

  private final LocalVariableForLambdas localVariableForLambdas = new LocalVariableForLambdas();

  @Test
  void mustSum(){
    // arrange

    // act
    final var sum = this.localVariableForLambdas.sum(4, 6);

    // assert
    assertEquals(10, sum);
  }
}
