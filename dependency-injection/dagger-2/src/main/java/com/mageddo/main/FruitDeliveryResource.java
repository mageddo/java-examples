package com.mageddo.main;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FruitDeliveryResource {

  private final FruitDeliveryService fruitDeliveryService;

  @Inject
  public FruitDeliveryResource(FruitDeliveryService fruitDeliveryService) {
    this.fruitDeliveryService = fruitDeliveryService;
  }

  public void deliver(String fruitName) {
    this.fruitDeliveryService.deliver(fruitName);
  }
}
