package com.mageddo.fruit.service;

import java.util.UUID;
import java.util.List;

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

    this.service.createIfAbsent(expected);

    assertFruitEqualsIgnoringMetadata(this.service.find(expected.getId()), expected);
  }

  @Test
  void createIfAbsentShouldKeepStoredDataWhenExists() {
    final var expected = FruitTemplates.banana();
    final var overwriteAttempt = FruitTemplates.updatedBanana();
    this.service.createIfAbsent(expected);

    this.service.createIfAbsent(overwriteAttempt);

    assertFruitEqualsIgnoringMetadata(this.service.find(expected.getId()), expected);
  }

  @Test
  void createIfAbsentShouldReportCreationWhenMissing() {
    final var fruit = FruitTemplates.banana();

    final var created = this.service.createIfAbsent(fruit);

    assertThat(created).isTrue();
  }

  @Test
  void createIfAbsentShouldReportNoCreationWhenExists() {
    final var fruit = FruitTemplates.banana();
    this.service.createIfAbsent(fruit);

    final var created = this.service.createIfAbsent(fruit);

    assertThat(created).isFalse();
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

  @Test
  void findByNameShouldReturnMatches() {
    this.create(FruitTemplates.banana());
    this.create(FruitTemplates.greenBanana());

    final var out = this.service.findByName("Banana");
    assertThat(out)
        .usingRecursiveComparison()
        .ignoringFields("createdAt", "updatedAt")
        .isEqualTo(List.of(
            FruitTemplates.banana(),
            FruitTemplates.greenBanana()
        ));
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
