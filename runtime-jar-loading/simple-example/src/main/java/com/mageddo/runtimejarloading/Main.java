package com.mageddo.runtimejarloading;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
  public static void main(String[] args) throws Exception {

    final URLClassLoader jarClassLoader = createClassLoader(Paths.get(args[0]));
    final Class<?> helloWorldClass = loadClass(
        jarClassLoader,
        "com.mageddo.runtimejarloading.helloworldlibrary.HelloWorldLibrary"
    );
    final Object instance = helloWorldClass.getDeclaredConstructor().newInstance();
    final Method method = helloWorldClass.getDeclaredMethod("helloWorld");
    final Object result = method.invoke(instance);
    System.out.printf("result: %s%n", result);

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
