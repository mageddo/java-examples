package com.mageddo.fruit.config.ebean;

import com.mageddo.fruit.dataprovider.FruitRow;
import com.mageddo.referrer.dataprovider.jpa.ReferrerRow;

import jakarta.transaction.TransactionSynchronizationRegistry;

import jakarta.transaction.UserTransaction;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.agroal.api.AgroalDataSource;
import io.ebean.Database;
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
      @ConfigProperty(name = "quarkus.datasource.jdbc.database") String databaseName,
      TransactionSynchronizationRegistry registry,
      UserTransaction userTransaction
  ) {
    return Database
        .builder()
        .ddlGenerate(false)
        .ddlRun(false)
        .runMigration(false)
        .name(databaseName)
        .externalTransactionManager(new QuarkusEbeanTransactionManager(
            registry, userTransaction
        ))
        .dataSource(dataSource)
        .addClass(FruitRow.class)
        .addClass(ReferrerRow.class)
        .build();
  }

}
