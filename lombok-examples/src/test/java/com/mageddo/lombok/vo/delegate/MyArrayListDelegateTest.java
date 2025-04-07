package com.mageddo.lombok.vo.delegate;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MyArrayListDelegateTest {

  @Test
  public void mustCountAddItems() {

    // arrange

    final List<String> fruits = new MyArrayListDelegate<>();

    // act
    fruits.add("orange");
    fruits.add("apple");
    fruits.addAll(List.of("grape", "tomato"));

    // assert
    assertEquals(4, ((MyArrayListDelegate) fruits).getAddItems());
    assertEquals(4, fruits.size());

  }

}
