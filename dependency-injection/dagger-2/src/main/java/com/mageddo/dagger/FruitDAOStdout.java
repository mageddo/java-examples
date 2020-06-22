package com.mageddo.dagger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FruitDAOStdout implements FruitDAO {

  @Inject
  public FruitDAOStdout() {
  }

  @Override
  public void deliver(String fruitName) {
    System.out.printf("%s was delivered", fruitName);
  }
}
