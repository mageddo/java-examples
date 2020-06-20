package com.mageddo.runtimejarloading;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;

import samepackage.Calculator;

public class Main {
  public static void main(String[] args) throws Exception {

    final URLClassLoader jarClassLoader = createClassLoader(Paths.get(args[0]));
    {
      System.out.println("Simple class loading and method execution");
      System.out.println("------------------------------");
      final Class<?> helloWorldClass = loadClass(
          jarClassLoader,
          "com.mageddo.runtimejarloading.helloworldlibrary.HelloWorldLibrary"
      );
      final Object instance = helloWorldClass.getDeclaredConstructor().newInstance();
      final Method method = helloWorldClass.getDeclaredMethod("helloWorld");
      final Object result = method.invoke(instance);
      System.out.printf("result: %s%n", result);
    }

    System.out.println();
    System.out.println("Testing conflicting classpath");
    System.out.println("------------------------------");

    final int a = 7;
    final int b = 3;
    System.out.printf("from main jar: %d + %d=%d%n", a, b, Calculator.sum(a, b));

    {
      final Class<?> calculatorClass = loadClass(jarClassLoader, "samepackage.Calculator");
      final Method sumMethod = calculatorClass.getDeclaredMethod("sum", int.class, int.class);
      System.out.printf("from loaded jar: %d + %d=%d%n", a, b, sumMethod.invoke(null, a, b));
    }

  }

  static Class<?> loadClass(URLClassLoader classLoader, String className) throws ClassNotFoundException {
    return Class.forName(
        className,
        true,
        classLoader
    );
  }

  static URLClassLoader createClassLoader(Path jar) throws MalformedURLException {
    return new URLClassLoader(
        String.format("%s-classloader", jar.getFileName()),
        new URL[]{jar.normalize().toUri().toURL()},
        Class.class.getClassLoader()
    );
  }
}
