package com.mageddo.fruit.config;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import com.mageddo.fruit.dataprovider.FruitRow;
import jakarta.inject.Singleton;
import jakarta.inject.Inject;
import jakarta.enterprise.inject.Produces;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class EbeanDatabaseProducer {

  private final DataSource dataSource;

  @Produces
  @Singleton
  public Database database() {
    final var databaseConfig = new DatabaseConfig();
    databaseConfig.setName("db");
    databaseConfig.setDbSchema("ebean_orm");
    databaseConfig.setDataSource(this.dataSource);
    databaseConfig.addClass(FruitRow.class);
    return DatabaseFactory.create(databaseConfig);
  }
}
