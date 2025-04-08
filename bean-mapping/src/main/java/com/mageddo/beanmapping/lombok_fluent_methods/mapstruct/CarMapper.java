package com.mageddo.beanmapping.lombok_fluent_methods.mapstruct;

import com.mageddo.beanmapping.lombok_fluent_methods.Car;
import com.mageddo.beanmapping.lombok_fluent_methods.CarVO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarMapper {

  CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

  CarVO of(Car car);

}
