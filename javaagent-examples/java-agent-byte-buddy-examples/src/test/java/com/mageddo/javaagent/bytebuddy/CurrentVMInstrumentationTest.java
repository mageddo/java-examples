package com.mageddo.javaagent.bytebuddy;

import java.lang.instrument.Instrumentation;

import net.bytebuddy.agent.ByteBuddyAgent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CurrentVMInstrumentationTest {
  @Test
  void mustInstallToCurrentVmAndListClasses(){
    // act
    final Instrumentation instrumentation = ByteBuddyAgent.install();

    // assert
    assertTrue(instrumentation.getAllLoadedClasses().length > 0);
  }

}
