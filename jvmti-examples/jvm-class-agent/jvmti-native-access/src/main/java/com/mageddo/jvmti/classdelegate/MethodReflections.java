package com.mageddo.jvmti.classdelegate;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

@UtilityClass
public class MethodReflections {

  @SneakyThrows
  public static Method getMethod(Class<?> jClass, String name, Class<?> ... args){
    final Method m = getMethod0(jClass, name, args);
    m.setAccessible(true);
    return m;
  }

  public static Method getMethod(Class<?> jClass, String name, Object ... args) {
    return getMethod(
      jClass,
      name,
      Stream
        .of(args)
        .map(Object::getClass)
        .toArray(Class[]::new)
    );
  }

  @SneakyThrows
  public static Object invoke(Object instance, String name, Object ... args){
    return getMethod(instance.getClass(), name, args).invoke(instance, args);
  }

  public static Set<Method> getMethods(Class<?> jClass) {
    final Set<Method> fields = new LinkedHashSet<>();
    fields.addAll(Arrays.asList(jClass.getMethods()));
    fields.addAll(Arrays.asList(jClass.getDeclaredMethods()));
    return fields;
  }

  @SneakyThrows
  static Method getMethod0(Class<?> jClass, String name, Class<?> ... args){
    try {
      return jClass.getMethod(name, args);
    } catch (NoSuchMethodException e) {
      return jClass.getDeclaredMethod(name, args);
    }
  }
}
