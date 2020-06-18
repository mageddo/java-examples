package com.mageddo.jvmti.classdelegate;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public class FieldReflections {

  public static Field getField(Class<?> jClass, String name) {
    final Field f = getField0(jClass, name);
    f.setAccessible(true);
    return f;
  }

  @SneakyThrows
  static Field getField0(Class<?> jClass, String name) {
    try {
      return jClass.getField(name);
    } catch (NoSuchFieldException e) {
      return jClass.getDeclaredField(name);
    }
  }
}
