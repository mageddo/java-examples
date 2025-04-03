package com.mageddo.beanmapping.templates;

import java.time.LocalDateTime;

import com.mageddo.beanmapping.AccessToken;
import com.mageddo.beanmapping.vo.AccessTokenV1;

public class AccessTokenV1Templates {
  public static AccessTokenV1 build() {
    return AccessTokenV1
        .builder()
        .accessToken("access_token")
        .refreshToken("refresh_token")
        .tokenType(AccessToken.TokenType.Bearer)
        .expiresAt(LocalDateTime.now())
        .build();
  }
}
