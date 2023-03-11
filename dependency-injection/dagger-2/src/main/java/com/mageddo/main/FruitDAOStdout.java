package com.mageddo.main;

import javax.inject.Inject;

public class FruitDAOStdout implements FruitDAO {

  @Inject
  public FruitDAOStdout() {
  }

  @Override
  public void deliver(String fruitName) {
    System.out.printf("%s was delivered%n", fruitName);
  }
}
