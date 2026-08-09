package com.mageddo.fruit.service;

import java.util.UUID;

import com.mageddo.fruit.FruitService;
import com.mageddo.fruit.Fruit;
import com.mageddo.fruit.domain.templates.FruitTemplates;
import com.mageddo.testing.DatabaseConfiguratorExtension;
import com.mageddo.testing.DatabaseConfigurator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(DatabaseConfiguratorExtension.class)
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
    final var expected = FruitTemplates.bananaWithReferrer();

    final var out = this.service.createIfAbsent(expected);

    assertFruitEqualsIgnoringMetadata(out, expected);
  }

  @Test
  void createIfAbsentShouldKeepStoredDataWhenExists() {
    final var expected = FruitTemplates.banana();
    final var overwriteAttempt = FruitTemplates.updatedBanana();

    this.service.createIfAbsent(expected);

    final var out = this.service.createIfAbsent(overwriteAttempt);

    assertFruitEqualsIgnoringMetadata(out, expected);
  }

  @Test
  void saveShouldUpsertAndFindShouldReturnSaved() {
    final var created = FruitTemplates.greenBananaWithReferrer();

    this.create(created);

    final var out = this.service.save(FruitTemplates.greenBananaWithReferrerUpdated());

    assertFruitEqualsIgnoringMetadata(out, FruitTemplates.greenBananaWithReferrerUpdated());

    final var found = this.service.find(created.getId());

    assertFruitEqualsIgnoringMetadata(found, FruitTemplates.greenBananaWithReferrerUpdated());
  }

  @Test
  void createAndFailShouldRollbackTransaction() {
    final var fruit = FruitTemplates.banana();

    assertThatThrownBy(() -> this.service.createAndFail(fruit))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("failed");

    assertThat(this.service.find(fruit.getId()))
        .isNull();
  }

  @Test
  void findShouldReturnNullWhenMissing() {
    final var missing = UUID.randomUUID();

    assertThat(this.service.find(missing))
        .isNull();
  }

  void assertFruitEqualsIgnoringMetadata(Fruit out, Fruit expected) {
    assertThat(out)
        .usingRecursiveComparison()
        .ignoringFields("createdAt", "updatedAt")
        .isEqualTo(expected);
  }

  void create(Fruit fruit) {
    this.service.save(fruit);
  }
}
