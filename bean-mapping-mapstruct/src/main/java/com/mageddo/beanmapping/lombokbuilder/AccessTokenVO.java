package com.mageddo.beanmapping.lombokbuilder;


import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@ToString
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AccessTokenVO {

  @NonNull
  String accessToken;

  @NonNull
  String refreshToken;

  @NonNull
  LocalDateTime expiresAt;

  @NonNull
  AccessToken.TokenType tokenType;

}
