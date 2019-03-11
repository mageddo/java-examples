package com.huongdanjava.aspectj;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldTest {

	@Test
	public void aspectJShouldProxyHelloMethod(){

		final String msg = new HelloWorld().hello("Maria");

		assertEquals("Hello World!!! (Proxied)", msg);
	}

}
