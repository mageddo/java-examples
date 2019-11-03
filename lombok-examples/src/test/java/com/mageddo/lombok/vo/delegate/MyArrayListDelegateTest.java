package com.mageddo.lombok.vo.delegate;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MyArrayListDelegateTest {

	@Test
	public void mustCountAddItems(){

		// arrange

		final var fruits = new MyArrayListDelegate();

		// act
		fruits.add("orange");
		fruits.add("apple");
		fruits.addAll(List.of("grape", "tomato"));

		// assert
		assertEquals(4, fruits.getAddItems());
		assertEquals(4, fruits.size());

	}

}
