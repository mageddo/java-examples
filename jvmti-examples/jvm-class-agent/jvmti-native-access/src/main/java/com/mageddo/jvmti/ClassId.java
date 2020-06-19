package com.mageddo.jvmti;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.Value;

@Value
@EqualsAndHashCode(of = "className")
public class ClassId {

  private String className;

  @SneakyThrows
  public Class<?> toClass() {
    return Class.forName(this.className);
  }

  public static ClassId of(String className) {
    return new ClassId(className);
  }
}
