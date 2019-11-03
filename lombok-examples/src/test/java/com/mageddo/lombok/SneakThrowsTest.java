package com.mageddo.lombok;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class SneakThrowsTest {

	@Test
	public void mustSuppressCheckedException(){
		// arrange

		// act
		printHi();

		// assert
	}

	@Test
	public void mustCatchGenericExceptionWhenCheckedExceptionFail() throws Exception {
		// arrange
		val mockPrintStream = mock(PrintStream.class);
		doThrow(new IOException("can't print hi")).when(mockPrintStream).write(any(byte[].class));

		System.setOut(mockPrintStream);

		// act
		try {
			printHi();
			fail();
		} catch (Exception e){
			assertEquals("can't print hi", e.getMessage());
		}

		// assert
	}

	@SneakyThrows
	private void printHi() {
		System.out.write("hi".getBytes());
	}
}
