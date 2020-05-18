package com.mageddo.java14.switchexpressions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SwitchExpressionsTest {

  private final SwitchExpressions switchExpressions = new SwitchExpressions();

  @Test
  void mustFailToFindColor() {
    // arrange

    // act
    // assert
    assertThrows(UnsupportedOperationException.class, () -> {
      this.switchExpressions.findColor(-1);
    });

  }

  @Test
  void mustFindOrange() {
    // arrange

    // act
    final var colorName = this.switchExpressions.findColor(2);

    // assert
    assertEquals("Orange", colorName);
  }

  @Test
  void mustSumParameters() {
    // arrange

    // act
    final var sum = this.switchExpressions.calc("+", 1, 2);

    // assert
    assertEquals(3, sum);
  }

}
