package com.mageddo.core.exception;

public class TooManyRequestsException extends BusinessException {

  public TooManyRequestsException(String msg) {
    super(msg);
  }
}
