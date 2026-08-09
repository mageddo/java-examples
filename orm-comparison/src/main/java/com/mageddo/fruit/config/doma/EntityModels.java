package com.mageddo.fruit.config.doma;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

public final class EntityModels {

  private EntityModels() {
  }

  private static final ClassValue<MethodHandle> CONSTRUCTORS = new ClassValue<>() {

    @Override
    protected MethodHandle computeValue(Class<?> rowClass) {
      try {
        final var modelClassName =
            rowClass.getPackageName()
                + "."
                + rowClass.getSimpleName()
                + "_";

        final var modelClass = Class.forName(
            modelClassName,
            true,
            rowClass.getClassLoader()
        );

        return MethodHandles.publicLookup()
            .findConstructor(
                modelClass,
                MethodType.methodType(void.class)
            );

      } catch (ReflectiveOperationException e) {
        throw new IllegalArgumentException(
            "Doma entity model not found for " + rowClass.getName(),
            e
        );
      }
    }
  };

  @SuppressWarnings("unchecked")
  public static <T> EntityMetamodel<T> get(Class<T> rowClass) {
    try {
      return (EntityMetamodel<T>) CONSTRUCTORS
          .get(rowClass)
          .invoke();
    } catch (Throwable e) {
      throw new IllegalStateException(
          "Unable to instantiate Doma entity model for " + rowClass.getName(),
          e
      );
    }
  }
}
