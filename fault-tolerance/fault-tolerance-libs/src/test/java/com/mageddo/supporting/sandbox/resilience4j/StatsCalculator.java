package com.mageddo.supporting.sandbox.resilience4j;

import java.io.UncheckedIOException;

import com.mageddo.supporting.sandbox.Result;
import com.mageddo.supporting.sandbox.Stats;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;

public class StatsCalculator {
  public static Result calcStats(Stats stats, Runnable r) {
    try {
      r.run();
      stats.success++;
      return Result.SUCCESS;
    } catch (CallNotPermittedException e) {
      stats.openCircuit++;
      return Result.CIRCUIT_OPEN;
    } catch (UncheckedIOException e) {
      stats.error++;
      return Result.ERROR;
    }
  }
}
