package com.mageddo.jvmti.classdelegate;

import lombok.Value;

import java.util.UUID;

@Value
public class InstanceId {

  UUID code;

  public static InstanceId of(UUID code) {
    return new InstanceId(code);
  }

  public static InstanceId of(Object o) {
    return of(UUID.nameUUIDFromBytes(String.valueOf(o.hashCode()).getBytes()));
  }
}
