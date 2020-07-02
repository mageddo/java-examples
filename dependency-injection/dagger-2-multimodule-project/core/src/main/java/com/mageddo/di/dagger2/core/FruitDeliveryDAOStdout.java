package com.mageddo.di.dagger2.core;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FruitDeliveryDAOStdout implements FruitDeliveryDAO {
  @Inject
  public FruitDeliveryDAOStdout() {
  }

  @Override
  public void deliver(String fruit) {
    System.out.println(fruit + " was delivered");
  }
}
