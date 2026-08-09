package com.mageddo.persistence;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * ORM ativo, escolhido em runtime pela property {@code orm.provider}.
 */
public enum OrmProvider {

  EBEAN,
  DOMA;

  public static OrmProvider of(String value) {
    return Arrays
        .stream(values())
        .filter(it -> it
            .name()
            .equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(String.format(
            "Unknown orm.provider: %s, expected one of: %s",
            value,
            Arrays
                .stream(values())
                .map(it -> it
                    .name()
                    .toLowerCase())
                .collect(Collectors.joining(", "))
        )));
  }
}
