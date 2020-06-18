package com.mageddo.jvmti.agents;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mageddo.jvmti.dependencies.DependencyInjectionModule;
import net.metzweb.tinyserver.TinyServer;

import java.lang.instrument.Instrumentation;

public class ClassAgent {

  private static Injector injector;

  public static void premain(String agentArgs, Instrumentation instrumentation) {
    System.out.println("pre main!!!!!!!!!!!!!!!!!!");
  }

  public static void agentmain(String agentArgs, Instrumentation instrumentation) {
    if (injector != null) {
      System.out.println("already started");
      return;
    }
    System.out.println("starting server");
    injector = Guice.createInjector(new DependencyInjectionModule());
    injector.getInstance(TinyServer.class).start();
  }
}