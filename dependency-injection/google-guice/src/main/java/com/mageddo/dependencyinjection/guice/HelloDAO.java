package com.mageddo.dependencyinjection.guice;

import javax.inject.Singleton;

@Singleton
public class HelloDAO {
  public String findHelloMsg() {
    return "Hello World!!!";
  }
}
