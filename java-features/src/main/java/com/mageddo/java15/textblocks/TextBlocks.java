package com.mageddo.java15.textblocks;

public class TextBlocks {

  public String fruits() {
    final var fruits = """
    Orange
    Apple
    Grape
    Melon
    """;
    return fruits;
  }

  public String fruitsWithWhitespace() {
    final var fruits = """
      Orange
      Apple
      Grape
      Melon
    """;
    return fruits;
  }

  public String stuff() {
    final var loremIpsum = """
            What is Lorem Ipsum?
            Lorem Ipsum is simply dummy text of the printing and typesetting industry.
            Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,
            when an unknown printer took a galley of type and scrambled it to make a type specimen book.
           It has survived not only five centuries, but also the leap into electronic typesetting,
           remaining essentially unchanged. It was popularised in the 1960s with the release of
           Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing
           software like Aldus PageMaker including versions of Lorem Ipsum.
        """;
    return loremIpsum;
  }


}
