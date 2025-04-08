package com.mageddo.beanmapping.lombok_fluent_methods.modelmapper;

import com.mageddo.beanmapping.lombok_fluent_methods.Car;
import com.mageddo.beanmapping.lombok_fluent_methods.CarVO;

import org.modelmapper.ModelMapper;

public class CarMapper {

  public static final ModelMapper MAPPER = buildMapper();

  private static ModelMapper buildMapper() {
    return new ModelMapper();
  }

  public static CarVO of(Car car){
    return MAPPER.map(car, CarVO.class);
  }
}
