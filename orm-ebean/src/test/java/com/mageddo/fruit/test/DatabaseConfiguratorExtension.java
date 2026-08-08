package com.mageddo.fruit.test;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import java.util.Map;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import javax.sql.DataSource;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class DatabaseConfiguratorExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback {

  private static final String DB_NAME = "db";
  private static final String ROOT_USER = "root";
  private static final String ROOT_PASSWORD = "root";
  private static final String SCHEMA = "ebean_orm";
  private static volatile EmbeddedPostgres embeddedPostgres;

  private final List<Consumer<EmbeddedPostgres.Builder>> builderCustomizers = new CopyOnWriteArrayList<>();

  public DatabaseConfiguratorExtension() {
    this.customize(builder -> builder.setPort(0));
  }

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    final var store = this.getStore(context);
    if (store.get(EmbeddedPostgres.class.getName(), EmbeddedPostgres.class) == null) {
      this.setNewInstanceOnContext(store);
    }
  }

  @Override
  public void afterAll(ExtensionContext context) throws Exception {
    final var store = this.getStore(context);
    final var instance = store.get(EmbeddedPostgres.class.getName(), EmbeddedPostgres.class);
    if (instance != null) {
      instance.close();
      store.remove(EmbeddedPostgres.class.getName());
    }
    embeddedPostgres = null;
  }

  @Override
  public void beforeEach(ExtensionContext context) {
  }

  public static EmbeddedPostgres postgres() {
    return embeddedPostgres;
  }

  public static String dbName() {
    return DB_NAME;
  }

  public static String rootUser() {
    return ROOT_USER;
  }

  public static String rootPassword() {
    return ROOT_PASSWORD;
  }

  public static String schema() {
    return SCHEMA;
  }

  public DatabaseConfiguratorExtension customize(Consumer<EmbeddedPostgres.Builder> customizer) {
    this.builderCustomizers.add(customizer);
    return this;
  }

  private ExtensionContext.Store getStore(ExtensionContext context) {
    return context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL);
  }

  private void setNewInstanceOnContext(ExtensionContext.Store store) throws Exception {
    final EmbeddedPostgres.Builder builder = EmbeddedPostgres.builder();
    this.builderCustomizers.forEach(customizer -> customizer.accept(builder));
    final EmbeddedPostgres instance = builder.start();
    embeddedPostgres = instance;
    store.put(EmbeddedPostgres.class.getName(), instance);

    try (var adminConnection = this.adminDataSource().getConnection();
        var statement = adminConnection.createStatement()) {
      try (var roles = statement.executeQuery("SELECT 1 FROM pg_roles WHERE rolname = 'root'")) {
        if (!roles.next()) {
          statement.execute("CREATE ROLE root LOGIN PASSWORD 'root'");
        }
      }
      try (var databases = statement.executeQuery("SELECT 1 FROM pg_database WHERE datname = 'db'")) {
        if (!databases.next()) {
          statement.execute("CREATE DATABASE db OWNER root");
        }
      }
    }

    try (var rootConnection = this.rootDataSource().getConnection();
        var statement = rootConnection.createStatement()) {
      statement.execute("CREATE SCHEMA IF NOT EXISTS ebean_orm AUTHORIZATION root");
    }

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        instance.close();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }));
  }

  private DataSource adminDataSource() {
    return this.embeddedPostgres.getPostgresDatabase();
  }

  private DataSource rootDataSource() {
    return this.embeddedPostgres.getDatabase(ROOT_USER, DB_NAME, credentials());
  }

  public static Map<String, String> credentials() {
    return Map.of("password", ROOT_PASSWORD);
  }
}
