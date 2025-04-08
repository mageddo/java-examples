package com.mageddo.beanmapping.lombok_fluent_methods.modelmapper;

import com.mageddo.beanmapping.lombok_fluent_methods.templates.CarTemplates;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CarMapperTest {
  @Test
  void wontMapBeanWithFluentMethods(){

    final var car =  CarTemplates.build();

    final var vo = CarMapper.of(car);

    assertThrows(AssertionFailedError.class, () -> {
      assertEquals("""
        CarVO(name="Ford Ka, color="Black", fabricationYear="2020")
        """.trim(),
          vo.toString()
      );
    });
  }
}
