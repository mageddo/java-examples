package com.mageddo.utils;

import java.util.ServiceLoader;

import org.slf4j.ILoggerFactory;
import org.slf4j.impl.Slf4jLoggerFactory;
import org.slf4j.spi.SLF4JServiceProvider;

public class Tmp {
  public static ILoggerFactory findBestLoggerFactory() {
    final var loggerFactory = ServiceLoader
        .load(SLF4JServiceProvider.class)
        .stream()
        .filter(it -> it.type().toString().startsWith("class ch.qos.logback.classic.spi"))
        .findFirst()
        .orElse(null);
    if (loggerFactory == null) {
      return new Slf4jLoggerFactory();
    }
    return loggerFactory
        .get()
        .getLoggerFactory();
  }
}
