package com.mageddo.beanmapping;

import com.mageddo.beanmapping.vo.AccessTokenV1;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccessTokenMapper {
  AccessTokenMapper INSTANCE = Mappers.getMapper(AccessTokenMapper.class);

  AccessToken of(AccessTokenV1 accessToken);
}
