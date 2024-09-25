package com.mageddo.polymorphictypes.bycustomfield;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.mageddo.polymorphictypes.bycustomfield.vo.Animal;
import com.mageddo.polymorphictypes.bycustomfield.vo.Cat;
import com.mageddo.polymorphictypes.bycustomfield.vo.Dog;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomFieldInterfaceTest {

  final ObjectMapper objectMapper = new ObjectMapper()
      .enable(SerializationFeature.INDENT_OUTPUT);

  @Test
  void mustParseHouseWithAnimal() {

  }

  @Test
  void mustSerializePolymorphicType() throws Exception {
    final var cat = new Cat("goya", "mice");
    final var json = objectMapper.writeValueAsString(cat);
    assertEquals("""
            {
              "__name" : "Cat",
              "name" : "goya",
              "favoriteToy" : "mice"
            }""",
        json
    );
  }

  @Test
  void mustParsePolymorphicType() throws Exception {


    {
      final var dog = new Dog("ruffus", "english shepherd");
      final var dogJson = objectMapper.writeValueAsString(dog);
      final var parsedDog = objectMapper.readValue(dogJson, Animal.class);
      assertEquals(Dog.class, parsedDog.getClass());

    }

    {
      final var cat = new Cat("goya", "mice");
      final var catJson = objectMapper.writeValueAsString(cat);
      final var parsedCat = objectMapper.readValue(catJson, Animal.class);
      assertEquals(Cat.class, parsedCat.getClass());
    }


  }
}
