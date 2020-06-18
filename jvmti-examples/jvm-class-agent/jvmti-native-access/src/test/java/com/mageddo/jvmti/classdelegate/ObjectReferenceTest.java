package com.mageddo.jvmti.classdelegate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    final ObjectReference invocationReturnRef = objectReference.invoke("setName", "Grape");

    // assert
    assertNull(invocationReturnRef);
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

    public void setName(String name) {
      this.name = name;
    }
  }
}