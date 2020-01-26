package com.mageddo.lombok;

import java.io.IOException;
import java.io.PrintStream;

import org.junit.Test;

import lombok.SneakyThrows;
import lombok.val;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class SneakThrowsTest {

  @Test
  public void mustSuppressCheckedException() {
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
    val lastOut = System.out;

    // act
    try {
      System.setOut(mockPrintStream);
      printHi();
      fail();
    } catch (Exception e) {
      assertEquals("can't print hi", e.getMessage());
    } finally {
      System.setOut(lastOut);
    }

    // assert
  }

  @SneakyThrows
  private void printHi() {
    System.out.write("hi".getBytes());
  }
}
