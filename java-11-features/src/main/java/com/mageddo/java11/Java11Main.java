package com.mageddo.java11;

public class Java11Main {
  public static void main(String[] args) {
    System.out.println("# JEP 323: Local-Variable Syntax for Lambda Parameters");
    final var localVariableForLambdas = new LocalVariableForLambdas();

    final var sum = localVariableForLambdas.sum(3, 6);
    System.out.printf("sum = %d%n%n", sum);

  }
}
