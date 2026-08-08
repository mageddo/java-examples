package com.mageddo.fruit.service;

import com.mageddo.fruit.dataprovider.FruitDAO;
import com.mageddo.fruit.domain.Fruit;

import jakarta.inject.Singleton;

import java.util.UUID;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class FruitService {

  private final FruitDAO fruitDao;

  @Transactional
  public Fruit createIfAbsent(Fruit fruit) {
    final var created = this.fruitDao.createIfAbsent(fruit);
    log.info("created={}, fruit={}", created, fruit);
    return this.fruitDao.find(fruit.getId());
  }

  @Transactional
  public Fruit save(Fruit fruit) {
    return this.fruitDao.save(fruit);
  }

  public Fruit find(UUID id) {
    return this.fruitDao.find(id);
  }
}
