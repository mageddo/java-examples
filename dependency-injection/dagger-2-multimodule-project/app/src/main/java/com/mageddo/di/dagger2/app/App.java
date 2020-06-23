package com.mageddo.di.dagger2.app;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class App {

  @Inject
  FruitDeliveryResource fruitDeliveryResource;

  public static void main(String[] args) {
    firstWay();
    secondWay();
  }

  void deliver(String fruit) {
    this.fruitDeliveryResource.deliver(fruit);
  }


  static void firstWay() {
    AppFactory
        .getInstance()
        .fruitDeliveryResource()
        .deliver("Grape");
  }

  static void secondWay() {
    final App app = new App();
    AppFactory
        .getInstance()
        .inject(app);
    app.deliver("Orange");
  }

}
