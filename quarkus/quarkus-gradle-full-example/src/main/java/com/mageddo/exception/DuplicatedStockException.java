package com.mageddo.exception;

public class DuplicatedStockException extends RuntimeException {
  public DuplicatedStockException(String msg) {
    super(msg);
  }
}
