package com.mageddo.resilience4j.supporting;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;

import com.mageddo.resilience4j.supporting.resilience4j.StateMapper;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

public class Resilience4jCircuitBreakerSandBox extends AbstractCircuitBreakerSandBox {

  private final CircuitBreaker circuitBreaker;

  public Resilience4jCircuitBreakerSandBox(final CircuitBreaker circuitBreaker) {
    this.circuitBreaker = circuitBreaker;
  }

  @Override
  protected State getCircuitBreakerState() {
    return StateMapper.from(this.circuitBreaker.getState());
  }

  @Override
  public String runError() {
    return runError(this.circuitBreaker);
  }

  @Override
  public String runSuccess() {
    return runSuccess(this.circuitBreaker);
  }

  public static String runError(CircuitBreaker breaker) {
    return breaker.executeSupplier(() -> {
      throw new UncheckedIOException(new IOException("an error"));
    });
  }

  public static String runSuccess(CircuitBreaker breaker) {
    return breaker.executeSupplier(() -> LocalDateTime.now().toString());
  }

}
