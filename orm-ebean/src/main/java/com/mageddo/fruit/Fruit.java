package com.mageddo.fruit;

import java.time.Instant;
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

  @NonNull
  Instant createdAt;

  @NonNull
  Instant updatedAt;

}
