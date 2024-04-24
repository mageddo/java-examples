package com.mageddo.java21.stringtemplates;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * https://openjdk.org/jeps/430
 */
class StringTemplatesTest {

  @Test
  void mustEvaluateString() {

    // arrange

    final var name = "John";
    final var age = 23;
    final var pair = Pair.of("color", "green");

    // act
    final String tpl = StringTemplate.STR."""
        Hello \{name}, your age is \{age}, in the next year you will be \{age + 1}.
        \{pair.getKey()}=\{pair.getValue()}
        """;

    // assert
    assertEquals("""
        Hello John, your age is 23, in the next year you will be 24.
        color=green
        """, tpl);
  }
}
