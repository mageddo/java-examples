package com.mageddo.beanmapping.templates;

import java.time.LocalDateTime;

import com.mageddo.beanmapping.lombokbuilder.AccessToken;
import com.mageddo.beanmapping.lombokbuilder.AccessTokenVO;

public class AccessTokenV1Templates {
  public static AccessTokenVO build() {
    return AccessTokenVO
        .builder()
        .accessToken("access_token")
        .refreshToken("refresh_token")
        .tokenType(AccessToken.TokenType.Bearer)
        .expiresAt(LocalDateTime.now())
        .build();
  }
}
