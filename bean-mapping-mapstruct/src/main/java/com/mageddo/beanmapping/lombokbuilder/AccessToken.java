package com.mageddo.beanmapping.lombokbuilder;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class AccessToken {

  @NonNull
  String accessToken;

  @NonNull
  String refreshToken;

  @NonNull
  LocalDateTime expiresAt;

  @NonNull
  AccessToken.TokenType tokenType;

  public boolean isNotExpired() {
    throw new UnsupportedOperationException();
  }

  public enum TokenType {
    Bearer
  }
}
