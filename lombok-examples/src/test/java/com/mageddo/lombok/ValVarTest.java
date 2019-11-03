package com.mageddo.lombok;

import lombok.val;
import lombok.var;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValVarTest {

	@Test
	public void mustCompileAndRunUsingValAndVarStatements(){

		// arrange

		// act
		var a = 0;
		var b = 2;

		a += 2;

		val sum = a + b;

		// won't compile
		// sum++;

		// assert
		assertEquals(4, sum);

	}
}
