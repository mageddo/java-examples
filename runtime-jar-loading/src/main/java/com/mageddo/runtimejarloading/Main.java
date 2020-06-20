package com.mageddo.runtimejarloading;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
  public static void main(String[] args) throws ClassNotFoundException, MalformedURLException,
      NoSuchMethodException, IllegalAccessException, InvocationTargetException,
      InstantiationException {
    final URLClassLoader classLoader = createClassLoader(Paths.get(args[0]));
    final Class<?> helloWorldClass = Class.forName(
        "com.mageddo.runtimejarloading.helloworldlibrary.HelloWorldLibrary",
        true,
        classLoader
    );
    final Object instance = helloWorldClass.getDeclaredConstructor().newInstance();
    final Method method = helloWorldClass.getDeclaredMethod("helloWorld");
    try {
      final Object result = method.invoke(instance);
      System.out.printf("result: %s%n", result);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  private static URLClassLoader createClassLoader(Path jar) throws MalformedURLException {
    return new URLClassLoader(
        String.format("%s-classloader", jar.getFileName()),
        new URL[]{jar.normalize().toUri().toURL()},
        Class.class.getClassLoader()
    );
  }
}
