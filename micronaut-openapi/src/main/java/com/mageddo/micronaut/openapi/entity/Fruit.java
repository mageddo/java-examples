package com.mageddo.micronaut.openapi.entity;

import io.swagger.v3.oas.annotations.media.Schema;

public class Fruit {

  private final String name;

  public Fruit(String name) {
    this.name = name;
  }

  @Schema(allowableValues = {"Orange", "Grape", "Apple"})
  public String getName() {
    return name;
  }
}
