package com.mageddo.exception;

public class DuplicatedStockException extends RuntimeException {
  public DuplicatedStockException(Throwable e) {
    super(e);
  }
}
