package com.mageddo.jvmti.classdelegate;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public class FieldReflections {

  @SneakyThrows
  public static Field getField(Class<?> jClass, String name) {
    final Field f = getField0(jClass, name);
    f.setAccessible(true);
//    Field modifiersField = Field.class.getDeclaredField("modifiers");
//    modifiersField.setAccessible(true);
//    modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
    return f;
  }

  @SneakyThrows
  private static Field getField0(Class<?> jClass, String name) {
    try {
      return jClass.getField(name);
    } catch (NoSuchFieldException e) {
      return jClass.getDeclaredField(name);
    }
  }
}
