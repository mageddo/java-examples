package com.mageddo.micronaut.openapi.entity;

import io.swagger.v3.oas.annotations.media.Schema;

public class Fruit {

  @Schema(example = "Orange")
  private final String name;

  public Fruit(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
