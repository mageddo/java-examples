package com.mageddo.beanmapping.javarecords.mapstruct;

import com.mageddo.beanmapping.javarecords.FruitVO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FruitMapperTest {
  @Test
  void mustMapJavaRecordsClass(){
    final var vo = new FruitVO("Orange");

    final var fruit = FruitMapper.INSTANCE.of(vo);

    assertNotNull(fruit);
    assertEquals(fruit.name(), "Orange");
  }
}
