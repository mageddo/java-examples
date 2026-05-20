package com.mageddo.ws.rs;

import lombok.Getter;

@Getter
public class ApiClientException extends RuntimeException {

  private final int status;

  public ApiClientException(int status, String body) {
    super("status=%d, body=%s".formatted(status, body));
    this.status = status;
  }
}
