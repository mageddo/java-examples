package com.mageddo.jvmti;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(of = {"classId", "name", "argsTypes"})
public class MethodId {

  ClassId classId;

  String name;

  ClassId[] argsTypes;
}
