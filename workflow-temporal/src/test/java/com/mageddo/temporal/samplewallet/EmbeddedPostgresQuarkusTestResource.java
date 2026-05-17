package com.mageddo.temporal.samplewallet;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import java.sql.SQLException;
import java.util.Map;

public class EmbeddedPostgresQuarkusTestResource implements QuarkusTestResourceLifecycleManager {

  EmbeddedPostgres embeddedPostgres;

  @Override
  public Map<String, String> start() {
    try {
      this.embeddedPostgres = EmbeddedPostgres.start();
      this.initializeSchema();
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

  void initializeSchema() throws SQLException {
    try (var connection = this.embeddedPostgres.getPostgresDatabase().getConnection();
         var statement = connection.createStatement()) {
      statement.execute("""
        create table investor (
          id varchar(100) primary key,
          profile varchar(30) not null
        )
        """);
      statement.execute("""
        create table wallet (
          id varchar(100) primary key,
          investor_id varchar(100) not null,
          status varchar(30) not null,
          created_at timestamp with time zone not null,
          ready_at timestamp with time zone,
          aborted_at timestamp with time zone
        )
        """);
      statement.execute("""
        create table investment (
          id varchar(100) primary key,
          wallet_id varchar(100) not null,
          investor_id varchar(100) not null,
          base_investment_id varchar(100) not null,
          profile varchar(30) not null,
          created boolean not null
        )
        """);
      statement.execute("""
        create table financial_event_candidate (
          id varchar(100) primary key,
          investment_id varchar(100) not null,
          status varchar(30) not null,
          processed boolean not null,
          attempts integer not null,
          processed_at timestamp with time zone
        )
        """);
    }
  }
}
