package com.mageddo.jvmti.agents;

import com.mageddo.jvmti.Server;

import java.lang.instrument.Instrumentation;

public class ClassAgent {

  public static void premain(String agentArgs, Instrumentation instrumentation) {
    System.out.println("pre main!!!!!!!!!!!!!!!!!!");
  }

  public static void agentmain(String agentArgs, Instrumentation instrumentation) {
    Server.start();
  }
}