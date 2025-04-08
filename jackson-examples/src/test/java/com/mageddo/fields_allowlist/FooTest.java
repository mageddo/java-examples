package com.mageddo.fields_allowlist;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import lombok.SneakyThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FooTest {

  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @SneakyThrows
  void mustSerializeOnlyAllowedListFiles(){
    final Foo foo = new Foo();

    final var json = this.objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(foo);

    assertEquals("""
        {
          "sometext" : "Hello World",
          "somenumber" : 30
        }
        """.trim(), json
    );
  }

}
