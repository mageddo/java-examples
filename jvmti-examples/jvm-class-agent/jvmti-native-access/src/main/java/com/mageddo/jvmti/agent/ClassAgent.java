package com.mageddo.jvmti.agent;

import com.mageddo.jvmti.Server;
import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ClassAgent {

  private static boolean initialized = false;

  public static void premain(String agentArgs, Instrumentation instrumentation) {
    log.info("pre main!!!!!!!!!!!!!!!!!!");
  }

  public static void agentmain(String agentArgs, Instrumentation instrumentation) {
    if (initialized) {
      log.warn("status=agent-already-installed");
      return;
    }
    initialized = true;

    log.debug("server task submitted");
    final ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.submit(() -> {
      log.info("starting server...");
      Server.start();
    });
  }
}