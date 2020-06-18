package com.mageddo.jvmti.classdelegate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectReferenceTest {

  @Test
  void mustGetFieldValue(){
    // arrange
    final Fruit orange = new Fruit("Orange");
    final ObjectReference objectReference = new ObjectReference(orange);

    // act
    final ObjectReference nameReference = objectReference.getFieldValue("name");

    // assert
    assertEquals("ObjectReference(instance=Orange)", nameReference.toString());
  }

  @Test
  void mustChangeFruitName(){
    // arrange
    final Fruit orange = new Fruit("Orange");
    final ObjectReference objectReference = new ObjectReference(orange);

    // act
    final ObjectReference nameReference = objectReference.invoke("setName", "Grape");

    // assert
    assertEquals("Grape", orange.getName());
  }

  static class Fruit {

    private String name;

    Fruit(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public Fruit setName(String name) {
      this.name = name;
      return this;
    }
  }
}