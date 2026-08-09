package com.mageddo.testing;

import com.mageddo.basket.Basket;
import com.mageddo.basket.BasketDAO;
import com.mageddo.fruit.Fruit;
import com.mageddo.fruit.FruitDAO;

import io.ebean.Database;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * Cenários que precisam de mais de uma operação dentro da <b>mesma</b> transação, algo que os
 * services de produção não expõem por serem, cada um, transacionais por conta própria.
 */
@Singleton
@RequiredArgsConstructor
public class TransactionScenarios {

  private final FruitDAO fruitDAO;
  private final BasketDAO basketDAO;
  private final Database database;

  /**
   * Um conflito esperado no {@code createIfAbsent} não pode abortar a transação: depois dele
   * ainda existe trabalho a fazer em outra tabela, e o commit precisa acontecer.
   *
   * @return true se a fruta foi criada
   */
  @Transactional
  public boolean createIfAbsentThenCreateBasket(Fruit fruit, Basket basket) {
    final var fruitCreated = this.fruitDAO.createIfAbsent(fruit);
    this.basketDAO.createIfAbsent(basket);
    return fruitCreated;
  }

  /**
   * Mesma sequência da {@link #createIfAbsentThenCreateBasket(Fruit, Basket)}, mas terminando
   * em erro: um conflito absorvido no meio do caminho não pode deixar a transação num estado em
   * que o rollback não valha para o que veio depois.
   */
  @Transactional
  public void createIfAbsentThenCreateBasketAndFail(Fruit fruit, Basket basket) {
    this.fruitDAO.createIfAbsent(fruit);
    this.basketDAO.createIfAbsent(basket);
    throw new IllegalArgumentException("failed");
  }

  /**
   * Só faz sentido no Ebean: com o JDBC batch ligado o insert é enfileirado e o retorno do
   * {@code createIfAbsent} deixa de ser confiável.
   *
   * @see com.mageddo.fruit.config.ebean.EbeanInsertIfAbsent
   */
  @Transactional
  public boolean createIfAbsentInBatchMode(Fruit fruit) {
    this.database
        .currentTransaction()
        .setBatchMode(true);
    return this.fruitDAO.createIfAbsent(fruit);
  }
}
