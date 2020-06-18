package com.mageddo.jvmti;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InstanceValue {
  ClassId classId;
  String value;
}
