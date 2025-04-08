package com.mageddo.beanmapping.lombok_fluent_methods;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarVO {
  String name;
  String color;
  String fabricationYear;
}
