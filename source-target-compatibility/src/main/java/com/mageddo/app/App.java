package com.mageddo.app;

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
  }
}
