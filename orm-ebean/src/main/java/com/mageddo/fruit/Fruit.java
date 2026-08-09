package com.mageddo.fruit;

import java.util.UUID;
import com.mageddo.referrer.Referrer;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Fruit {

  @NonNull
  UUID id;

  @NonNull
  String name;

  String color;

  String season;

  Referrer referrer;

}
