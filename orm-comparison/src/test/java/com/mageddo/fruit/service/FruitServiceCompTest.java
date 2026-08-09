package com.mageddo.fruit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.mageddo.basket.BasketService;
import com.mageddo.basket.templates.BasketTemplates;
import com.mageddo.fruit.Fruit;
import com.mageddo.fruit.FruitService;
import com.mageddo.fruit.templates.FruitTemplates;
import com.mageddo.persistence.OrmProvider;
import com.mageddo.testing.DatabaseConfigurator;
import com.mageddo.testing.TransactionScenarios;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Cenários que todo ORM precisa satisfazer.
 * <p>
 * Cada subclasse liga um {@code orm.provider} diferente, então a suíte inteira roda uma vez
 * por ORM, um de cada vez.
 */
abstract class FruitServiceCompTest {

  static final int CONCURRENT_THREADS = 16;

  @Inject
  DatabaseConfigurator databaseConfigurator;

  @Inject
  FruitService service;

  @Inject
  BasketService basketService;

  @Inject
  TransactionScenarios scenarios;

  @Inject
  OrmProvider orm;

  abstract OrmProvider expectedOrm();

  @BeforeEach
  void beforeEach() {
    this.databaseConfigurator.truncate();
  }

  @Test
  void shouldRunAgainstTheSelectedOrm() {
    assertThat(this.orm).isEqualTo(this.expectedOrm());
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

  /**
   * "Não altera absolutamente nada" inclui os timestamps, que os demais asserts ignoram.
   * A comparação é entre duas leituras do banco para não esbarrar na precisão do
   * {@code TIMESTAMPTZ}.
   */
  @Test
  void createIfAbsentShouldNotTouchTimestampsWhenExists() {
    this.service.createIfAbsent(FruitTemplates.banana());
    final var stored = this.service.find(FruitTemplates.BANANA_ID);

    this.service.createIfAbsent(FruitTemplates.updatedBanana());

    assertThat(this.service.find(FruitTemplates.BANANA_ID))
        .usingRecursiveComparison()
        .isEqualTo(stored);
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
  void saveShouldReportCreationWhenMissing() {
    final var created = this.service.save(FruitTemplates.banana());

    assertThat(created).isTrue();
  }

  @Test
  void saveShouldReportUpdateWhenExists() {
    this.service.save(FruitTemplates.greenBananaWithReferrer());

    final var created = this.service.save(FruitTemplates.greenBananaWithReferrerUpdated());

    assertThat(created).isFalse();
  }

  @Test
  void saveShouldUpsertAndFindShouldReturnSaved() {
    final var created = FruitTemplates.greenBananaWithReferrer();

    this.create(created);

    this.service.save(FruitTemplates.greenBananaWithReferrerUpdated());

    final var found = this.service.find(created.getId());

    assertFruitEqualsIgnoringMetadata(found, FruitTemplates.greenBananaWithReferrerUpdated());
  }

  /**
   * O conflito é absorvido pelo banco, então a transação continua utilizável para o que vem
   * depois — aqui, um insert em outra tabela — e o commit acontece normalmente.
   */
  @Test
  void createIfAbsentConflictShouldKeepTransactionUsable() {
    final var basket = BasketTemplates.fruitBasket();
    this.service.createIfAbsent(FruitTemplates.banana());

    final var fruitCreated = this.scenarios.createIfAbsentThenCreateBasket(
        FruitTemplates.updatedBanana(), basket
    );

    assertThat(fruitCreated).isFalse();
    assertThat(this.basketService.find(basket.getId())).isNotNull();
    assertFruitEqualsIgnoringMetadata(
        this.service.find(FruitTemplates.BANANA_ID), FruitTemplates.banana()
    );
  }

  /**
   * A contraprova: absorver o conflito não pode custar o rollback do resto da unidade
   * transacional.
   */
  @Test
  void createIfAbsentConflictShouldNotPreventRollback() {
    final var basket = BasketTemplates.fruitBasket();
    this.service.createIfAbsent(FruitTemplates.banana());

    assertThatThrownBy(() -> this.scenarios.createIfAbsentThenCreateBasketAndFail(
        FruitTemplates.updatedBanana(), basket
    ))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("failed");

    assertThat(this.basketService.find(basket.getId())).isNull();
    assertFruitEqualsIgnoringMetadata(
        this.service.find(FruitTemplates.BANANA_ID), FruitTemplates.banana()
    );
  }

  /**
   * A resolução do conflito é do banco, em um único statement: sob concorrência exatamente uma
   * chamada cria a linha e nenhuma estoura exceção.
   */
  @Test
  void createIfAbsentShouldCreateOnceUnderConcurrency() throws Exception {
    final var results = this.runConcurrently(
        () -> this.service.createIfAbsent(FruitTemplates.banana())
    );

    assertThat(results).hasSize(CONCURRENT_THREADS);
    assertThat(results.stream().filter(Boolean::booleanValue)).hasSize(1);
    assertThat(this.service.find(FruitTemplates.BANANA_ID)).isNotNull();
  }

  @Test
  void saveShouldNotFailUnderConcurrency() throws Exception {
    final var results = this.runConcurrently(
        () -> this.service.save(FruitTemplates.banana())
    );

    assertThat(results).hasSize(CONCURRENT_THREADS);
    assertThat(results.stream().filter(Boolean::booleanValue)).hasSize(1);
    assertFruitEqualsIgnoringMetadata(
        this.service.find(FruitTemplates.BANANA_ID), FruitTemplates.banana()
    );
  }

  /**
   * O {@code GenericDAO} não sabe nada sobre Fruit: uma tabela nova entra no sistema sem SQL,
   * HQL ou query builder próprio.
   */
  @Test
  void genericDaoShouldWorkForAnotherEntity() {
    final var basket = BasketTemplates.fruitBasket();

    assertThat(this.basketService.createIfAbsent(basket)).isTrue();
    assertThat(this.basketService.createIfAbsent(BasketTemplates.fruitBasketUpdated())).isFalse();
    assertThat(this.basketService.find(basket.getId()).getName()).isEqualTo(basket.getName());

    assertThat(this.basketService.save(BasketTemplates.fruitBasketUpdated())).isFalse();
    assertThat(this.basketService.find(basket.getId()).getName())
        .isEqualTo(BasketTemplates.fruitBasketUpdated().getName());
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

  /**
   * Dispara a mesma operação em {@value #CONCURRENT_THREADS} threads que largam juntas, cada
   * uma na sua transação. Qualquer exceção em qualquer thread reprova o teste.
   */
  List<Boolean> runConcurrently(Callable<Boolean> operation) throws Exception {
    final var start = new CountDownLatch(1);

    try (final var executor = Executors.newFixedThreadPool(CONCURRENT_THREADS)) {

      final var futures = new ArrayList<Future<Boolean>>(CONCURRENT_THREADS);
      for (var i = 0; i < CONCURRENT_THREADS; i++) {
        futures.add(executor.submit(() -> {
          start.await();
          return operation.call();
        }));
      }

      start.countDown();

      final var results = new ArrayList<Boolean>(CONCURRENT_THREADS);
      for (final var future : futures) {
        results.add(future.get());
      }
      return results;
    }
  }
}
