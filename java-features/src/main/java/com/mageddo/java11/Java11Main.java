package com.mageddo.java11;

import java.io.IOException;

import com.mageddo.java11.flightrecorder.HelloWorldEvent;

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

    System.out.println("# JEP 328: Flight Recorder");
    final var event = new HelloWorldEvent();
    event.message = "Hello World!!";
    event.commit();
  }
}
