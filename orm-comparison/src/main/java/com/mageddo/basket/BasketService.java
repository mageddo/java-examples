package com.mageddo.basket;

import java.util.UUID;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class BasketService {

  private final BasketDAO basketDAO;

  /**
   * @return true se o basket foi criado, false se já existia
   */
  @Transactional
  public boolean createIfAbsent(Basket basket) {
    final var created = this.basketDAO.createIfAbsent(basket);
    log.info("created={}, basket={}", created, basket);
    return created;
  }

  /**
   * @return true se o basket foi criado, false se foi atualizado
   */
  @Transactional
  public boolean save(Basket basket) {
    return this.basketDAO.save(basket);
  }

  public Basket find(UUID id) {
    return this.basketDAO.find(id);
  }
}
