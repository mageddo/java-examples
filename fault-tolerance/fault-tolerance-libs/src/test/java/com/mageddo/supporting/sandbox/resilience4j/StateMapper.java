package com.mageddo.supporting.sandbox.resilience4j;

import com.mageddo.supporting.sandbox.State;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

public class StateMapper {
  public static State from(CircuitBreaker.State state) {
    return State.valueOf(state.name());
  }

}
