package com.mageddo.di.dagger2.app;

import com.mageddo.di.dagger2.core.FruitDeliveryService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FruitDeliveryResource {

  private final FruitDeliveryService fruitDeliveryService;

  @Inject
  public FruitDeliveryResource(FruitDeliveryService fruitDeliveryService) {
    this.fruitDeliveryService = fruitDeliveryService;
  }


  public void deliver(String fruit) {
    this.fruitDeliveryService.deliver(fruit);
  }
}
