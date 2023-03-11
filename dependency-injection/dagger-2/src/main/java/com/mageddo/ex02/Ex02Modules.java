package com.mageddo.ex02;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

public class Ex02Modules {

  /**
   * This bean doens't need a module because it's already defined as a Singleton and has a @Inject constructor.
   */
  @Singleton
  static class Bean {
    @Inject
    public Bean() {
    }

    public String helloWorld() {
      return getClass().getSimpleName() + ": Hello World!!";
    }
  }

  static class Bean2 {
    public String doStuff() {
      return getClass().getSimpleName() + ": Hello World!!";
    }
  }

  /**
   * Modules which aren't an interface must be set in Dagger build, they also can work with instance variables
   * and provide classes dynamically depending on runtime decisions.
   */
  @Module
  static class Module1 {
    @Provides
    Bean2 bean2() {
      return new Bean2();
    }
  }

  @Singleton
  @Component(modules = Module1.class)
  interface Graph {

    Bean bean();

    Bean2 bean2();

    static Graph create() {
      return DaggerEx02Modules_Graph
          .builder()
          .module1(new Module1())
          .build();
    }
  }

  public static void main(String[] args) {
    final var graph = Graph.create();
    System.out.println(graph.bean().helloWorld());
    System.out.println(graph.bean2().doStuff());
  }

}
