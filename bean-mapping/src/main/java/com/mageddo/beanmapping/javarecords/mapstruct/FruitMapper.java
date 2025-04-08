package com.mageddo.beanmapping.javarecords.mapstruct;

import com.mageddo.beanmapping.javarecords.Fruit;
import com.mageddo.beanmapping.javarecords.FruitVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FruitMapper {
  FruitMapper INSTANCE = Mappers.getMapper(FruitMapper.class);

  Fruit of(FruitVO fruit);
}
