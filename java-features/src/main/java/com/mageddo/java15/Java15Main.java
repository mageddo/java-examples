package com.mageddo.java15;

import com.mageddo.java15.textblocks.TextBlocks;

public class Java15Main {
  public static void main(String[] args) {
    System.out.println("# Text Blocks");
    final var textBlocks = new TextBlocks();
    System.out.printf("## fruits%n");
    System.out.println(textBlocks.fruits());

    System.out.printf("## fruits with whitespace on the left %n");
    System.out.println(textBlocks.fruitsWithWhitespace());
  }
}
