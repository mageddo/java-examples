package com.mageddo.beanmapping.lombokbuilder.mapstruct;

import com.mageddo.beanmapping.lombokbuilder.AccessToken;
import com.mageddo.beanmapping.lombokbuilder.AccessTokenVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccessTokenMapper {
  AccessTokenMapper INSTANCE = Mappers.getMapper(AccessTokenMapper.class);

  AccessToken of(AccessTokenVO accessToken);
}
