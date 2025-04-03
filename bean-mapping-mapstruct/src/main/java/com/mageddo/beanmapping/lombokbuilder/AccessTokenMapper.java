package com.mageddo.beanmapping.lombokbuilder;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccessTokenMapper {
  AccessTokenMapper INSTANCE = Mappers.getMapper(AccessTokenMapper.class);

  AccessToken of(AccessTokenVO accessToken);
}
