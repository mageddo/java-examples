package com.mageddo.javaagent.bytebuddy;

import net.bytebuddy.agent.ByteBuddyAgent;

import java.lang.instrument.Instrumentation;

public class Main {
  public static void main(String[] args) {
    final Instrumentation instrumentation = ByteBuddyAgent.install();
    System.out.println(instrumentation.getAllLoadedClasses());
  }
}
