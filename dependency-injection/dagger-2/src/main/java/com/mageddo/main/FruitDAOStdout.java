package com.mageddo.main;

import javax.inject.Inject;

/**
 * Obs: Beans are not Singleton by default, a new instance will be returned for every call.
 */
public class FruitDAOStdout implements FruitDAO {

  @Inject
  public FruitDAOStdout() {
  }

  @Override
  public void deliver(String fruitName) {
    System.out.printf("%s was delivered%n", fruitName);
  }
}
