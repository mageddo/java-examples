package com.mageddo.jvmti;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(of = {"name", "classId"})
public class FieldId {
  ClassId classId;
  String name;
}
