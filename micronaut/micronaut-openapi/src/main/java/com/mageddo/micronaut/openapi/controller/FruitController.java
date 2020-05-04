package com.mageddo.micronaut.openapi.controller;

import java.util.List;

import com.mageddo.micronaut.openapi.entity.Fruit;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.Operation;

@Controller
public class FruitController {

  @Get
  @Operation(summary = "some action to list fruits")
  public List<Fruit> fruitList() {
    return List.of(
        new Fruit("Apple"),
        new Fruit("Orange"),
        new Fruit("Grape")
    );
  }
}
