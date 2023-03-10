package com.mageddo.thymeleaf.quarkushotswapfix;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import lombok.SneakyThrows;

class FieldUtils {

  private static final VarHandle MODIFIERS;

  static {
    try {
      var lookup = MethodHandles.privateLookupIn(Field.class, MethodHandles.lookup());
      MODIFIERS = lookup.findVarHandle(Field.class, "modifiers", int.class);
    } catch (IllegalAccessException | NoSuchFieldException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static void makeNonFinal(Field field) {
    int mods = field.getModifiers();
    if (Modifier.isFinal(mods)) {
      MODIFIERS.set(field, mods & ~Modifier.FINAL);
    }
  }

  @SneakyThrows
  public static void writeStaticField(Field field, Object o) {
    makeNonFinal(field);
    org.apache.commons.lang3.reflect.FieldUtils.writeStaticField(field, o, true);
  }
}
