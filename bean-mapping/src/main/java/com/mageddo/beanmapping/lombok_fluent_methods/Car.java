package com.mageddo.beanmapping.lombok_fluent_methods;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder
@Accessors(fluent = true)
public class Car {
  String name;
  String color;
  int fabricationYear;
}
