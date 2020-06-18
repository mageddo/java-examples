package com.mageddo.dependencyinjection.guice;

import com.google.inject.Guice;

public class App {
  public static void main(String[] args) {
    Guice
        .createInjector(new DependencyInjectionModule())
        .getInstance(HelloResource.class)
        .sayHello();
  }
}
