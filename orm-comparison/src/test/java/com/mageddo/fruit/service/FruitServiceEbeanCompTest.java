package com.mageddo.fruit.service;

import com.mageddo.fruit.templates.FruitTemplates;
import com.mageddo.persistence.OrmProvider;
import com.mageddo.testing.DatabaseConfiguratorExtension;
import com.mageddo.testing.orm.EbeanTestProfile;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DatabaseConfiguratorExtension.class)
@QuarkusTest
@TestProfile(EbeanTestProfile.class)
class FruitServiceEbeanCompTest extends FruitServiceCompTest {

  @Override
  OrmProvider expectedOrm() {
    return OrmProvider.EBEAN;
  }

  /**
   * Guard da limitação documentada no {@link com.mageddo.fruit.config.ebean.EbeanInsertIfAbsent}:
   * com o JDBC batch ligado o insert fica enfileirado até o flush, o {@code postInsert} não roda
   * a tempo e o retorno vira um falso negativo — a linha é criada, mas o método diz que não.
   * <p>
   * O teste fixa esse comportamento para que uma eventual correção não passe despercebida.
   */
  @Test
  void createIfAbsentShouldReportFalseNegativeInBatchMode() {
    final var fruit = FruitTemplates.banana();

    final var created = this.scenarios.createIfAbsentInBatchMode(fruit);

    assertThat(created)
        .as("falso negativo esperado em batch mode")
        .isFalse();
    assertThat(this.service.find(fruit.getId()))
        .as("a linha é criada mesmo assim")
        .isNotNull();
  }
}
