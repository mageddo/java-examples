package com.mageddo.transactional.outbox.tobby;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Fruit {
  String name;
}
