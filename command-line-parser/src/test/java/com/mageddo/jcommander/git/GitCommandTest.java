package com.mageddo.jcommander.git;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GitCommandTest {

  @Test
  void buildCommander() {

    final ByteArrayOutputStream bout = new ByteArrayOutputStream();

    // arrange
    System.setOut(new PrintStream(bout));

    // act
    GitCommand.parseAndRun("status", "do.txt", "stuff.txt");

    // assert
    assertEquals(
        "git status command running, parameters=GitStatusCommand{paths=[do.txt, stuff.txt]}\n",
        bout.toString()
    );


  }
}
