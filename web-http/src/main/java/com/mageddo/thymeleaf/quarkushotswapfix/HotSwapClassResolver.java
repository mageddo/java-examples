package com.mageddo.thymeleaf.quarkushotswapfix;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ognl.ClassResolver;
import ognl.DefaultClassResolver;

/**
 * Fixing classloading when using quarkus hot swap,
 * see https://github.com/quarkusio/quarkus/issues/2809
 *
 * @see DefaultClassResolver
 */
public class HotSwapClassResolver extends Object implements ClassResolver {

  private final Map<String, Class<?>> classes = new ConcurrentHashMap<>(101);

  public HotSwapClassResolver() {
    super();
  }

  public Class<?> classForName(String className, Map context) throws ClassNotFoundException {
    Class<?> result = classes.get(className);
    if (result != null) {
      return result;
    }
    if ((className.indexOf('.') == -1)) {
      result = classForName("java.lang." + className);
    } else {
      result = classForName(className);
    }
    classes.put(className, result);
    return result;
  }

  private Class<?> classForName(String className) throws ClassNotFoundException {
    return Class.forName(
        className,
        false,
        Thread.currentThread()
            .getContextClassLoader()
    );
  }
}
