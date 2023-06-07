package com.mageddo.java11;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.function.BiFunction;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class LocalVariableForLambdas {

  public int sum(int a, int b) {
    return sumFunction().apply(a, b);
  }

  private BiFunction<Integer, Integer, Integer> sumFunction() {
    return (@SomeAnnotation var a, var b) -> {
      return a + b;
    };
  }


  @Target({PARAMETER})
  @Retention(RUNTIME)
  public @interface SomeAnnotation {
    /**
     * Returns whether or not the {@code Introspector} should
     * construct artifacts for the annotated method.
     * @return whether or not the {@code Introspector} should
     * construct artifacts for the annotated method
     */
    boolean value() default true;
  }

}
