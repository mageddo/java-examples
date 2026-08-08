package com.mageddo.fruit.config;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import com.mageddo.fruit.dataprovider.FruitRow;
import jakarta.inject.Singleton;
import jakarta.inject.Inject;
import jakarta.enterprise.inject.Produces;
import javax.sql.DataSource;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class EbeanDatabaseProducer {

  private final DataSource dataSource;
  @ConfigProperty(name = "quarkus.datasource.jdbc.url")
  @Inject
  String datasourceJdbcUrl;
  @ConfigProperty(name = "quarkus.flyway.schemas")
  @Inject
  String flywaySchemas;
  @ConfigProperty(name = "ebean.dbSchema")
  @Inject
  Optional<String> ebeanDbSchema;

  @Produces
  @Singleton
  public Database database() {
    final var databaseConfig = new DatabaseConfig();
    databaseConfig.setName(this.databaseName());
    databaseConfig.setDbSchema(this.databaseSchema());
    databaseConfig.setUseJtaTransactionManager(false);
    databaseConfig.setDataSource(this.dataSource);
    databaseConfig.addClass(FruitRow.class);
    return DatabaseFactory.create(databaseConfig);
  }

  private String databaseName() {
    if (this.datasourceJdbcUrl == null || this.datasourceJdbcUrl.isBlank()) {
      return "db";
    }

    if (!this.datasourceJdbcUrl.startsWith("jdbc:")) {
      return this.datasourceJdbcUrl;
    }

    try {
      final var uri = URI.create(this.datasourceJdbcUrl.substring("jdbc:".length()));
      final var path = uri.getPath();
      if (path == null || path.isBlank() || "/".equals(path)) {
        return "db";
      }
      return path.startsWith("/")
          ? path.substring(1)
          : path;
    } catch (IllegalArgumentException ignored) {
      return "db";
    }
  }

  private String databaseSchema() {
    return this.ebeanDbSchema
        .map(String::trim)
        .filter(value -> !value.isBlank())
        .orElseGet(this::flywaySchema);
  }

  private String flywaySchema() {
    if (this.flywaySchemas == null || this.flywaySchemas.isBlank()) {
      return "ebean_orm";
    }
    return Arrays.stream(this.flywaySchemas.split(","))
        .map(String::trim)
        .filter(value -> !value.isBlank())
        .findFirst()
        .orElse("ebean_orm");
  }
}
