package com.mageddo.fruit.config.doma;

import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MetaModels {

  private static final Map<Class<?>, Integer> ID_INDEX_CACHE =
      new ConcurrentHashMap<>();

  @SuppressWarnings("unchecked")
  public static <T> PropertyMetamodel<T> getIdProperty(EntityMetamodel<?> entity) {
    final var entityClass = entity
        .asType()
        .getEntityClass();

    final var index = ID_INDEX_CACHE.computeIfAbsent(
        entityClass,
        ignored -> findIdPropertyIndex(entity)
    );

    return (PropertyMetamodel<T>) entity
        .allPropertyMetamodels()
        .get(index);
  }

  private static int findIdPropertyIndex(EntityMetamodel<?> entity) {

    final var properties = entity.allPropertyMetamodels();

    for (var i = 0; i < properties.size(); i++) {
      if (properties
          .get(i)
          .asType()
          .isId()) {
        return i;
      }
    }

    throw new IllegalStateException(
        "Entity has no @Id: "
            + entity
            .asType()
            .getEntityClass()
            .getName()
    );
  }

  public static PropertyMetamodel<?> findIdProperty(EntityMetamodel<?> entity) {

    for (final var property : entity.allPropertyMetamodels()) {

      final var isId = property
          .asType()
          .isId();
      if (isId) {
        return property;
      }

    }

    throw new IllegalStateException(
        "Entity has no @Id: " + entity
            .asType()
            .getEntityClass()
            .getName()
    );
  }

}
