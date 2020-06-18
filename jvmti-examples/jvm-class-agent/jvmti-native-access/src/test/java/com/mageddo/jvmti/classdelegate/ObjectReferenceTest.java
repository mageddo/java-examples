package com.mageddo.jvmti.classdelegate;

import org.junit.jupiter.api.Disabled;
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

  @Disabled
  @Test
  void mustChangeFinalField(){
    // arrange
    final FinalFruit fruit = new FinalFruit();
    final ObjectReference fruitReference = new ObjectReference(fruit);

    // act
    fruitReference.setFieldValue("name", "Strawberry");

    // assert
    assertEquals("Strawberry", fruit.name);
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

  static class FinalFruit {
    final String name = "Tomato";
  }

}