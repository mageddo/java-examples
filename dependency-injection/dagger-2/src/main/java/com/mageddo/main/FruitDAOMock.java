package com.mageddo.main;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FruitDAOMock implements FruitDAO {

  private final List<String> delivered;

  @Inject
  public FruitDAOMock() {
    this.delivered = new ArrayList<>();
  }

  @Override
  public void deliver(String fruitName) {
    this.delivered.add(fruitName);
  }

  public List<String> getDelivered() {
    return delivered;
  }

  public void clear() {
    this.delivered.clear();
  }
}
