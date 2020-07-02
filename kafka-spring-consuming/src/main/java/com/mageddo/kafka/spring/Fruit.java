package com.mageddo.kafka.spring;

public class Fruit {

  private String name;

  public String getName() {
    return name;
  }

  public Fruit setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public String toString() {
    return "Fruit{" +
        "name='" + name + '\'' +
        '}';
  }
}
