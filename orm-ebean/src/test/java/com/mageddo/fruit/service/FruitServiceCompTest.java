package com.mageddo.fruit.service;

import com.mageddo.fruit.domain.Fruit;
import com.mageddo.fruit.domain.templates.FruitTemplates;
import com.mageddo.micronaut.DatabaseConfiguratorExtension;
import com.mageddo.testing.DatabaseConfigurator;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DatabaseConfiguratorExtension.class)
@QuarkusTestResource(DatabaseConfiguratorExtension.class)
@QuarkusTest
class FruitServiceCompTest {

  @Inject
  DatabaseConfigurator databaseConfigurator;

  @Inject
  FruitService service;

  @BeforeEach
  void beforeEach() {
    this.databaseConfigurator.truncate();
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

    this.create(created);

    final var out = this.service.save(FruitTemplates.greenBananaAltSeason());

    assertThat(out)
        .usingRecursiveComparison()
        .isEqualTo(FruitTemplates.greenBananaAltSeason());

    final var found = this.service.find(created.getId());

    assertThat(found)
        .usingRecursiveComparison()
        .isEqualTo(FruitTemplates.greenBananaAltSeason());
  }

  @Test
  void findShouldReturnNullWhenMissing() {
    final var missing = UUID.randomUUID();

    assertThat(this.service.find(missing))
        .isNull();
  }

  void create(Fruit fruit) {
    this.service.save(fruit);
  }
}
