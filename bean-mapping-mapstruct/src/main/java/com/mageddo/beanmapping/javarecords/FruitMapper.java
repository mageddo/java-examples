package com.mageddo.beanmapping.javarecords;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FruitMapper {
  FruitMapper INSTANCE = Mappers.getMapper(FruitMapper.class);

  Fruit of(FruitVO fruit);
}
