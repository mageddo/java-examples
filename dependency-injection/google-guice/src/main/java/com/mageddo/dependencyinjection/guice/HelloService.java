package com.mageddo.dependencyinjection.guice;

import javax.inject.Singleton;

@Singleton
public class HelloService {

  private final HelloDAO helloDAO;

  public HelloService(HelloDAO helloDAO) {
    this.helloDAO = helloDAO;
  }

  public String sayHello() {
    return this.helloDAO.findHelloMsg();
  }
}
