package com.mageddo.temporal.samplewallet;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import java.util.Map;

public class EmbeddedPostgresQuarkusTestResource implements QuarkusTestResourceLifecycleManager {

  EmbeddedPostgres embeddedPostgres;

  @Override
  public Map<String, String> start() {
    try {
      this.embeddedPostgres = EmbeddedPostgres.start();
      return Map.of(
        "quarkus.datasource.jdbc.url", this.embeddedPostgres.getJdbcUrl("postgres", "postgres"),
        "quarkus.datasource.username", "postgres",
        "quarkus.datasource.password", "postgres"
      );
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void stop() {
    if (this.embeddedPostgres == null) {
      return;
    }
    try {
      this.embeddedPostgres.close();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
