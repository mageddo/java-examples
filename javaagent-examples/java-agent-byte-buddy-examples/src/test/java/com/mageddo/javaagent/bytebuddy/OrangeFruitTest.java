package com.mageddo.javaagent.bytebuddy;

import net.bytebuddy.agent.ByteBuddyAgent;

import org.junit.jupiter.api.Test;

import java.lang.instrument.Instrumentation;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class OrangeFruitTest {

  @Test
  void mustInstallToCurrentVmAndFINDOrangeFruit(){
    // arrange
    new OrangeFruit();
    final Instrumentation instrumentation = ByteBuddyAgent.install();
    // act
    final long found = Arrays
        .stream(instrumentation.getAllLoadedClasses())
        .filter(it -> it.getName().equals(OrangeFruit.class.getName()))
        .count()
        ;

    // assert
    assertEquals(1, found);
  }
}
