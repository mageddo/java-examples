package com.mageddo.di.dagger2.core;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FruitDeliveryService {

  private final FruitDeliveryDAO fruitDeliveryDAO;

  @Inject
  public FruitDeliveryService(FruitDeliveryDAO fruitDeliveryDAO) {
    this.fruitDeliveryDAO = fruitDeliveryDAO;
  }

  public void deliver(String fruit) {
    this.fruitDeliveryDAO.deliver(fruit);
  }
}
