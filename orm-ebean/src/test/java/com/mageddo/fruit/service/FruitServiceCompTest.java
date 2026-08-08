package com.mageddo.fruit.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.mageddo.fruit.dataprovider.FruitDAOEbean;
import com.mageddo.fruit.dataprovider.FruitRow;
import com.mageddo.fruit.domain.templates.FruitTemplates;
import com.mageddo.fruit.test.DatabaseConfiguratorExtension;
import io.ebean.Database;
import io.ebean.DatabaseConfig;
import io.ebean.DatabaseFactory;
import javax.sql.DataSource;
import java.util.UUID;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(DatabaseConfiguratorExtension.class)
public class FruitServiceCompTest {

  private FruitService service;

  private Database database;

  @BeforeAll
  void setUp() {
    final var ebeanDataSource = this.postgresDataSource();
    Flyway.configure()
        .dataSource(ebeanDataSource)
        .schemas(DatabaseConfiguratorExtension.schema())
        .defaultSchema(DatabaseConfiguratorExtension.schema())
        .locations("classpath:db/migration")
        .load()
        .migrate();

    final var databaseConfig = new DatabaseConfig();
    databaseConfig.setName("db");
    databaseConfig.setDbSchema(DatabaseConfiguratorExtension.schema());
    databaseConfig.setUseJtaTransactionManager(false);
    databaseConfig.setDataSource(ebeanDataSource);
    databaseConfig.addClass(FruitRow.class);
    this.database = DatabaseFactory.create(databaseConfig);
    this.service = new FruitService(new FruitDAOEbean(this.database));
  }

  @BeforeEach
  void beforeEach() {
    this.database.createUpdate("delete from " + DatabaseConfiguratorExtension.schema() + ".fruit").execute();
  }

  @Test
  void createIfAbsentShouldPersistWhenMissing() {
    final var expected = FruitTemplates.banana();
    final var out = this.service.createIfAbsent(expected);

    assertThat(out)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  @Test
  void createIfAbsentShouldKeepStoredDataWhenExists() {
    final var expected = FruitTemplates.banana();
    final var overwriteAttempt = FruitTemplates.updatedBanana();
    this.service.createIfAbsent(expected);
    final var out = this.service.createIfAbsent(overwriteAttempt);

    assertThat(out)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  @Test
  void saveShouldUpsertAndFindShouldReturnSaved() {
    final var created = FruitTemplates.greenBanana();
    this.service.save(created);
    final var upserted = this.service.save(FruitTemplates.greenBananaAltSeason());
    final var out = this.service.find(upserted.getId());

    assertThat(out)
        .usingRecursiveComparison()
        .isEqualTo(upserted);
    assertThat(created.getId())
        .isEqualTo(upserted.getId());
  }

  @Test
  void findShouldReturnNullWhenMissing() {
    final var missing = UUID.randomUUID();

    assertThat(this.service.find(missing))
        .isNull();
  }

  private DataSource postgresDataSource() {
    return DatabaseConfiguratorExtension.postgres().getDatabase(
        DatabaseConfiguratorExtension.rootUser(),
        DatabaseConfiguratorExtension.dbName(),
        DatabaseConfiguratorExtension.credentials()
    );
  }
}
