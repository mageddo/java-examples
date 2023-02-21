package com.mageddo.app;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {
  public static void main(String[] args) {
    System.out.println("> Text blocks");
    System.out.println("""
        Hello World
        from text blocks!!!
        """
    );
    System.out.println();

    System.out.println("> Switch expressions");
    final var msg = switch (System.getProperty("os.arch")) {
      case "amd64" -> "AMD 64 ARCH!";
      default -> System.getProperty("os.arch");
    };
    System.out.printf(msg);
    System.out.println();

    System.out.println("> Collection Streams");
    final var list = Stream
        .of(1, 2, 3, 4)
//        .toList() // wont work
        .collect(Collectors.toList())
        ;

    System.out.println(list);
    System.out.println();
  }
}
