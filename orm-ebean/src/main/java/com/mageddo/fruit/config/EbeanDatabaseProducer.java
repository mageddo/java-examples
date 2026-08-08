package com.mageddo.fruit.config;

import com.mageddo.fruit.dataprovider.FruitRow;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.agroal.api.AgroalDataSource;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class EbeanDatabaseProducer {

  @Produces
  @Singleton
  public Database database(
      AgroalDataSource dataSource,
      @ConfigProperty(name = "quarkus.datasource.jdbc.database") String databaseName
  ) {
    final var databaseConfig = new DatabaseConfig();
    databaseConfig.ddlGenerate(false);
    databaseConfig.ddlRun(false);
    databaseConfig.runMigration(false);
    databaseConfig.setName(databaseName);
    databaseConfig.setUseJtaTransactionManager(false);
    databaseConfig.setDataSource(dataSource);
    databaseConfig.addClass(FruitRow.class);
    return DatabaseFactory.create(databaseConfig);
  }

}
