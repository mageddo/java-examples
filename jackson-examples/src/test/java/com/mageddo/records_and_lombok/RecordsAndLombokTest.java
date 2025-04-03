package com.mageddo.records_and_lombok;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;

import org.junit.jupiter.api.Test;

import lombok.SneakyThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecordsAndLombokTest {

  final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @SneakyThrows
  void mustDeserializeJavaRecord() {
    final var json = """
        {"name":"Orange","weight":1}
        """;

    final var fruit = this.objectMapper.readValue(json, Fruit.class);
    assertEquals("Fruit[name=Orange, weight=1, weightInKg=0.0]", fruit.toString());

  }

  @Test
  @SneakyThrows
  void mustValidateNameWhenDeserializing() {
    final var json = """
        {"name":null,"weight":1,"weight_in_kg":1}
        """;

    final var ex = assertThrows(
        ValueInstantiationException.class,
        () -> this.objectMapper.readValue(json, Fruit.class)
    );

    assertTrue(ex
        .getMessage()
        .contains("name is marked non-null but is null"), ex.getMessage());

  }

  @Test
  @SneakyThrows
  void mustSerialize() {
    final var fruit = new Fruit("Orange", 1500, 1.5);

    final var json = this.objectMapper.writeValueAsString(fruit);

    assertEquals("""
            {"name":"Orange","weight":1500,"weight_in_kg":1.5}
            """.trim(),
        json
    );
  }
}

