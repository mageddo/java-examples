package com.mageddo.resilience4j.supporting.resilience4j;

import com.mageddo.resilience4j.supporting.State;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

public class StateMapper {
  public static State from(CircuitBreaker.State state) {
    return State.valueOf(state.name());
  }
}
