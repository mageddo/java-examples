package com.mageddo.beanmapping;

import com.mageddo.beanmapping.templates.AccessTokenV1Templates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccessTokenMapperTest {

  /**
   * O vo de origem não pode ser do tipo fluent.
   */
  @Test
  void mustMapLombokVoFluentAccessVo(){

    final var vo = AccessTokenV1Templates.build();

    final var accessToken = AccessTokenMapper.INSTANCE.of(vo);

    assertNotNull(accessToken);
    assertNotNull(accessToken.accessToken());
  }
}
