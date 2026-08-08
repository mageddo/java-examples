package com.mageddo.micronaut;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.sql.Statement;
import javax.sql.DataSource;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class DatabaseConfiguratorExtension implements QuarkusTestResourceLifecycleManager, BeforeAllCallback,
    AfterAllCallback {

  private static final String DB_NAME = "db";
  private static final String ROOT_USER = "root";
  private static final String ROOT_PASSWORD = "root";
  private static final String SCHEMA = "ebean_orm";
  private static final int PORT = 5434;

  private static volatile EmbeddedPostgres embeddedPostgres;

  private final List<Consumer<EmbeddedPostgres.Builder>> builderCustomizers = new CopyOnWriteArrayList<>();

  public DatabaseConfiguratorExtension() {
    this.customize(builder -> builder.setPort(PORT));
  }

  @Override
  public Map<String, String> start() {
    this.startDatabase();
    this.prepareDatabase();

    return Map.of(
        "quarkus.datasource.username", ROOT_USER,
        "quarkus.datasource.password", ROOT_PASSWORD,
        "quarkus.datasource.jdbc.url", this.jdbcUrl(),
        "quarkus.datasource.jdbc.driver", "org.postgresql.Driver",
        "quarkus.datasource.db-kind", "postgresql",
        "ebean.dbSchema", SCHEMA
    );
  }

  @Override
  public void stop() {
    final var instance = embeddedPostgres;
    if (instance != null) {
      try {
        instance.close();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    embeddedPostgres = null;
  }

  public DatabaseConfiguratorExtension customize(Consumer<EmbeddedPostgres.Builder> customizer) {
    this.builderCustomizers.add(customizer);
    return this;
  }

  public static EmbeddedPostgres postgres() {
    return embeddedPostgres;
  }

  public static Map<String, String> credentials() {
    return Map.of("password", ROOT_PASSWORD);
  }

  public static String schema() {
    return SCHEMA;
  }

  @Override
  public void beforeAll(ExtensionContext context) {
    this.start();
  }

  @Override
  public void afterAll(ExtensionContext context) {
    this.stop();
  }

  private void startDatabase() {
    if (embeddedPostgres != null) {
      return;
    }

    final EmbeddedPostgres.Builder builder = EmbeddedPostgres.builder();
    this.builderCustomizers.forEach(builder::accept);
    try {
      embeddedPostgres = builder.start();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      if (embeddedPostgres != null) {
        try {
          embeddedPostgres.close();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
      embeddedPostgres = null;
    }));
  }

  private void prepareDatabase() {
    try (var postgresConnection = this.adminDataSource().getConnection();
        var postgresStatement = postgresConnection.createStatement()) {
      this.createRoleIfNotExists(postgresStatement);
      this.createDatabaseIfNotExists(postgresStatement);
    }

    try (var dbConnection = this.rootDataSource().getConnection();
        var dbStatement = dbConnection.createStatement()) {
      dbStatement.execute("CREATE SCHEMA IF NOT EXISTS " + SCHEMA + " AUTHORIZATION root");
    }
  }

  private void createRoleIfNotExists(Statement statement) {
    try (var roles = statement.executeQuery("SELECT 1 FROM pg_roles WHERE rolname='root'")) {
      if (!roles.next()) {
        statement.execute("CREATE ROLE root LOGIN PASSWORD 'root'");
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void createDatabaseIfNotExists(Statement statement) {
    try (var databases = statement.executeQuery("SELECT 1 FROM pg_database WHERE datname='" + DB_NAME + "'")) {
      if (!databases.next()) {
        statement.execute("CREATE DATABASE " + DB_NAME + " OWNER root");
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private DataSource adminDataSource() {
    return this.embeddedPostgres.getPostgresDatabase();
  }

  private DataSource rootDataSource() {
    return this.embeddedPostgres.getDatabase(ROOT_USER, DB_NAME, credentials());
  }

  private String jdbcUrl() {
    return String.format(
        "jdbc:postgresql://localhost:%s/%s?user=%s&password=%s&currentSchema=%s",
        embeddedPostgres.getPort(),
        DB_NAME,
        ROOT_USER,
        ROOT_PASSWORD,
        SCHEMA
    );
  }
}
