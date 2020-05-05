package com.mageddo.micronaut.embeddedpostgres;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import lombok.SneakyThrows;

public class Postgres {
  @SneakyThrows
  public static EmbeddedPostgres start() {
    final var postgresInstance = EmbeddedPostgres
        .builder()
        .setPort(5436)
        .start()
    ;
    return postgresInstance;
  }
}
