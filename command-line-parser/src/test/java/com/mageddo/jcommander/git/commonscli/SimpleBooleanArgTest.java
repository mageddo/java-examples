package com.mageddo.jcommander.git.commonscli;

import com.mageddo.commonscli.SimpleBooleanArgs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleBooleanArgTest {
  @Test
  void mustHaveOption() throws Exception {

    // arrange
    final var args = new String[]{"-t"};

    // act
    assertTrue(SimpleBooleanArgs.isTrue(args));
  }

  @Test
  void mustNotHaveOption() throws Exception {

    // arrange
    final var args = new String[]{};

    // act
    assertFalse(SimpleBooleanArgs.isTrue(args));
  }
}
