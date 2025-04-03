package com.mageddo.beanmapping;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
@Accessors(fluent = true)
public class AccessToken {

  @NonNull
  String accessToken;

  @NonNull
  String refreshToken;

  @NonNull
  LocalDateTime expiresAt;

  @NonNull
  AccessToken.TokenType tokenType;

//  @NonNull
//  String scope;

  public boolean isNotExpired() {
    throw new UnsupportedOperationException();
  }

  public enum TokenType {
    Bearer
  }
}
