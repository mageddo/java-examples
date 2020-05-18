package com.mageddo.java11;

import java.io.IOException;

public class Java11Main {
  public static void main(String[] args) throws IOException, InterruptedException {

    System.out.println("# JEP 323: Local-Variable Syntax for Lambda Parameters");
    final var localVariableForLambdas = new LocalVariableForLambdas();
    final var sum = localVariableForLambdas.sum(3, 6);
    System.out.printf("sum = %d%n%n", sum);

    System.out.println("# JEP 321: HTTP Client (Standard)");
    final var httpClients = new HttpClients();
    final var result = httpClients.get("http://acme.com");
    System.out.printf("crawling http://acme.com = %s%n%n", result.substring(0, 100));
  }
}
