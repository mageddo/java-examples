package com.mageddo.commonscli.git;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GitTest {

  @Test
  void mustGetPathsToCheckStatus() throws ParseException {

    // arrange
    final var args = new String[]{"-status", "a.txt", "b.txt"};

    // act
    final var obj = Git.parse(args);

    // assert
    assertEquals("[a.txt, b.txt]", obj.getFiles().toString());

  }

}
