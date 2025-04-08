package com.mageddo.beanmapping.lombok_fluent_methods.templates;

import com.mageddo.beanmapping.lombok_fluent_methods.Car;

public class CarTemplates {
  public static Car build() {
    return Car
        .builder()
        .name("Ford Ka")
        .color("Black")
        .fabricationYear(2016)
        .build();
  }
}
