package com.mageddo.jvmti;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(of = "name")
public class FieldId {
  String name;
}
