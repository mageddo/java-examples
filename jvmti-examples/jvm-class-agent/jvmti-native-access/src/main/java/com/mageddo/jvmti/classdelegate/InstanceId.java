package com.mageddo.jvmti.classdelegate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@EqualsAndHashCode(of = "code")
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class InstanceId {

  UUID code;

  public static InstanceId of(UUID code) {
    return new InstanceId(code);
  }

  public static InstanceId of(Object o) {
    return of(UUID.nameUUIDFromBytes(String.valueOf(o.hashCode()).getBytes()));
  }
}
