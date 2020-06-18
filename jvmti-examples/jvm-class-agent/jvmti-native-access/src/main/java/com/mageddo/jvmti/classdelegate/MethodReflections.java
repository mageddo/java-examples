package com.mageddo.jvmti.classdelegate;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.util.stream.Stream;

@UtilityClass
public class MethodReflections {

  @SneakyThrows
  static Method getMethod(Class<?> jClass, String name, Class<?> ... parameters){
    final Method m = getMethod0(jClass, name, parameters);
    m.setAccessible(true);
    return m;
  }

  @SneakyThrows
  static Method getMethod0(Class<?> jClass, String name, Class<?> ... parameters){
    try {
      return jClass.getMethod(name, parameters);
    } catch (NoSuchMethodException e) {
      return jClass.getDeclaredMethod(name, parameters);
    }
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
}
