package com.mageddo.codestyle;

import java.util.List;

import static java.util.Collections.singletonMap;

public class Stuff {
  public void aVeryLongMethodNameExplainingWhatItDoes(String argumentA, String argumentB,
      String argumentC, String argumentD, String argumentE, String argumentF
  ) {
    System.out.println("do stuff");
  }

  public static void main(String[] args) {

    final List<String> fruits = List.of("apple", "orange");

    final var myObject = singletonMap("key", "value");

    new Stuff().aVeryLongMethodNameExplainingWhatItDoes(
        "argument A value", "argument B value", "string for argument C", "argument D value",
        "argument E value", "argument F value"
    );

    if (1 == 1) {


    }

    for (int i = 0; i < 10; i++) {

    }
  }
}
