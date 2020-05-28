package com.mageddo.usecase.exception;

public class DuplicatedStockException extends RuntimeException {
  public DuplicatedStockException(Throwable e) {
    super(e);
  }
}
