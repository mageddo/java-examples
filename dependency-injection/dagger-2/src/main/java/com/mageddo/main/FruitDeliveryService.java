package com.mageddo.main;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FruitDeliveryService {

  private final FruitDAO fruitDAO;

  @Inject
  public FruitDeliveryService(FruitDAO fruitDAO) {
    this.fruitDAO = fruitDAO;
  }

  public void deliver(String fruitName) {
    this.fruitDAO.deliver(fruitName);
  }
}
