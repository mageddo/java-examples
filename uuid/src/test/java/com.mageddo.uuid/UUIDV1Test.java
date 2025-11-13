package com.mageddo.uuid;

import com.fasterxml.uuid.Generators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UUIDV1Test {

  @Test
  void mustParseV1(){

    final var id = Generators.timeBasedGenerator().generate();

    assertEquals(1, id.version());
  }
}
