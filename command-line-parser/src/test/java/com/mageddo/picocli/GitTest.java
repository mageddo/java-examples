package com.mageddo.picocli;

import com.mageddo.picocli.git.Git;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GitTest {

  @Test
  void mustParseGitStatus() {

    // arrange
    final var args = new String[]{"status", "do.txt", "stuff.txt"};

    // act
    final var status = Git.parse(args);

    // assert
    assertEquals(
        "[do.txt, stuff.txt]"
        ,
        status.getPaths().toString()
    );

  }
}
