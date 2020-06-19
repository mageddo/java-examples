package com.mageddo.jvmti;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.Value;

import java.util.stream.Stream;

@Value
@EqualsAndHashCode(of = "className")
public class ClassId {

  private String className;

  public static ClassId[] of(Class<?>[] argsTypes) {
    return Stream.of(argsTypes)
    .map(ClassId::of)
    .toArray(ClassId[]::new);
  }

  @SneakyThrows
  public Class<?> toClass() {
    return Class.forName(this.className);
  }

  public static ClassId of(Class<?> jClass) {
    return of(jClass.getName());
  }

  public static ClassId of(String className) {
    return new ClassId(className);
  }
}
