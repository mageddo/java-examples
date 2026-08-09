package com.mageddo.fruit.config.doma;

import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MetaModels {

  private static final Map<Class<?>, PropertyMetamodel<?>> ID_CACHE =
      new ConcurrentHashMap<>();

  @SuppressWarnings("unchecked")
  public static <ENTITY, ID> PropertyMetamodel<ID> getIdProperty(
      EntityMetamodel<ENTITY> entity
  ) {
    final var entityClazz = entity
        .asType()
        .getEntityClass();
    return (PropertyMetamodel<ID>) ID_CACHE.computeIfAbsent(
        entityClazz, ignored -> findIdProperty(entity)
    );
  }

  static PropertyMetamodel<?> findIdProperty(EntityMetamodel<?> entity) {

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
