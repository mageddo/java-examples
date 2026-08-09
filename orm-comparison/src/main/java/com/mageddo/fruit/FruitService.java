package com.mageddo.fruit;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.inject.Named;

import java.util.UUID;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class FruitService {

  private final FruitDAO fruitDAO;

  @Transactional
  public Fruit createIfAbsent(Fruit fruit) {
    final var created = this.fruitDAO.createIfAbsent(fruit);
    log.info("created={}, fruit={}", created, fruit);
    return this.fruitDAO.find(fruit.getId());
  }

  @Transactional
  public Fruit createAndFail(Fruit fruit) {
    this.fruitDAO.createIfAbsent(fruit);
    throw new IllegalArgumentException("failed");
  }

  @Transactional
  public Fruit save(Fruit fruit) {
    final var created = this.fruitDAO.save(fruit);
    final var found = this.fruitDAO.find(fruit.getId());
    log.debug("status=done, created={}", created);
    return found;
  }

  public Fruit find(UUID id) {
    return this.fruitDAO.find(id);
  }
}
