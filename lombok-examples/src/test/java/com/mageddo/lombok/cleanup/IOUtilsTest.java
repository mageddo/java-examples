package com.mageddo.lombok.cleanup;

import lombok.val;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class IOUtilsTest {

	@Test
	public void mustReadInputStreamAndClose() throws Exception {

		// arrange
		val expectedString = "hello world!!!";
		val in = spy(new ByteArrayInputStream(expectedString.getBytes()));

		// act
		val readString = IOUtils.readToStringAndClose(in);

		// assert
		assertEquals(expectedString, readString);
		verify(in).close();

	}
}
