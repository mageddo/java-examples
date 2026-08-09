package com.mageddo.fruit;

import java.util.UUID;
import lombok.Value;

@Value
public class Fruit {
  UUID id;
  String name;
  String color;
  String season;
}
