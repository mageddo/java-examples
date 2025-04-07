package com.mageddo.lombok;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanupTest {

  @Test
  public void mustReadInputStreamAndClose() {

    // arrange
    val expectedString = "hello world!!!";
    val in = new InputStreamStub(new ByteArrayInputStream(expectedString.getBytes()));

    // act
    val readString = readToStringAndClose(in);

    // assert
    assertEquals(expectedString, readString);
    assertTrue(in.isClosed());

  }

  @SneakyThrows
  String readToStringAndClose(InputStream in) {
    @Cleanup var out = new ByteArrayOutputStream();
    @Cleanup var in0 = in;
    IOUtils.copy(in0, out);
    return out.toString();
  }

  static class InputStreamStub extends InputStream {

    private boolean closed;
    private final ByteArrayInputStream delegate;

    public InputStreamStub(ByteArrayInputStream delegate) {
      this.delegate = delegate;
    }

    @Override
    public void close() throws IOException {
      this.closed = true;
      super.close();
    }

    @Override
    public int read() throws IOException {
      return this.delegate.read();
    }

    public boolean isClosed() {
      return closed;
    }
  }
}
