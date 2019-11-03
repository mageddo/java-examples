package com.mageddo.lombok;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class CleanupTest {

	@Test
	public void mustReadInputStreamAndClose() throws Exception {

		// arrange
		val expectedString = "hello world!!!";
		val in = spy(new ByteArrayInputStream(expectedString.getBytes()));

		// act
		val readString = readToStringAndClose(in);

		// assert
		assertEquals(expectedString, readString);
		verify(in).close();

	}

	@SneakyThrows
	String readToStringAndClose(InputStream in) {
		@Cleanup val in0 = in;
		val bout = new ByteArrayOutputStream();
		val buff = new byte[512];
		for(; ; ){
			val i = in0.read(buff);
			if(i == -1) {
				return bout.toString();
			}
			bout.write(buff, 0, i);
		}
	}
}
