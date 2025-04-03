package com.mageddo.beanmapping.vo;


import java.time.LocalDateTime;

import com.mageddo.beanmapping.AccessToken;

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
public class AccessTokenV1 {

  @NonNull
  String accessToken;

  @NonNull
  String refreshToken;

  @NonNull
  LocalDateTime expiresAt;

  @NonNull
  AccessToken.TokenType tokenType;

}
