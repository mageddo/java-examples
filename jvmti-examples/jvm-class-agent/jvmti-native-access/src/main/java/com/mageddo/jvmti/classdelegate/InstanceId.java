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
    final String key = String.valueOf(String.format("%s-%s", o.getClass().getName(), o.hashCode()));
    return of(UUID.nameUUIDFromBytes(key.getBytes()));
  }
}
