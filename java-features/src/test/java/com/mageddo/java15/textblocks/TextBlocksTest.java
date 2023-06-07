package com.mageddo.java15.textblocks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextBlocksTest {

  public static final String LINE_SEPARATOR = System.getProperty("line.separator");
  private final TextBlocks textBlocks = new TextBlocks();

  /**
   * It must produce a string with five lines, starting with Orange and the last line will
   * contains a \n, the indentation will be trimmed and it depends on the position of the
   * text blocks close statement
   */
  @Test
  void mustReturn4FruitsOneByLine() {
    // arrange

    // act
    final var fruits = this.textBlocks.fruits();
    final var fruitsArray = fruits.split(LINE_SEPARATOR);

    // assert
    assertEquals(4, fruitsArray.length);
    assertTrue(fruits.startsWith("O"), fruits);
    assertTrue(fruits.endsWith("n\n"), fruits);
    assertTrue(fruitsArray[1].startsWith("Apple"), fruits);
  }

  @Test
  void mustReturn4FruitsWithSpaceOnTheLeft() {
    // arrange

    // act
    final var fruits = this.textBlocks.fruitsWithWhitespace();
    final var fruitsArray = fruits.split(LINE_SEPARATOR);

    // assert
    assertEquals(4, fruitsArray.length);
    assertTrue(fruits.startsWith("  O"), fruits);
    assertTrue(fruits.endsWith("n\n"), fruits);
    assertTrue(fruitsArray[1].startsWith("  Apple"), fruits);
  }
}
