package com.mageddo.commonscli;

import com.mageddo.commonscli.SimpleBooleanArgs;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleBooleanArgTest {
  @Test
  void mustHaveOption() throws Exception {

    // arrange
    final var args = new String[]{"-t"};
    final var obj = SimpleBooleanArgs.parse(args);

    // act
    assertTrue(obj.isTrue());
  }

  @Test
  void mustNotHaveOption() throws Exception {

    // arrange
    final var args = new String[]{};
    final var obj = SimpleBooleanArgs.parse(args);

    // act
    assertFalse(obj.isTrue());
  }

  @Test
  void mustGenerateHelp() throws ParseException {
    // arrange
    final var args = new String[]{"-h"};

    // act
    final var obj = SimpleBooleanArgs.parse(args);

    // assert
    assertEquals(
        """
        usage: koeh
         -h   Help
         -t   display current time
        """,
        obj.help()
    );
  }
}
