package com.mageddo.jvmti;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(of = {"classId", "name", "argsTypes"})
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class MethodId {

  ClassId classId;

  String name;

  ClassId[] argsTypes;
}
