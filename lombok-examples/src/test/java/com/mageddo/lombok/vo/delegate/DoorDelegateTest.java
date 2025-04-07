package com.mageddo.lombok.vo.delegate;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DoorDelegateTest {

  @Test
  public void mustDelegateDoor() {

    // arrange
    final var bout = new ByteArrayOutputStream();
    System.setOut(new PrintStream(bout));
    final var doorDelegate = new DoorDelegate();

    // act
    doorDelegate.open();
    doorDelegate.close();

    // assert
    assertEquals("door was opened\n" +
        "delegating door closing\n" +
        "the door was closed\n", bout.toString());

  }

}
