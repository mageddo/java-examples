package com.mageddo.java14.npeexceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NullPointerExceptionsTest {

  private final NullPointerExceptions nullPointerExceptions = new NullPointerExceptions();

  @Test
  void mustGetAFriendlyMessageForNpe(){
    // arrange
    final var expectedExceptionMsg = "Cannot invoke \"java.util.Map.get(Object)\" because the " +
        "return value of \"java.util.Map.get(Object)\" is null";

    // act
    final var exception = assertThrows(
        NullPointerException.class,
        this.nullPointerExceptions::treeValueFind
    );

    // assert
    assertEquals(expectedExceptionMsg, exception.getMessage());
  }

}
