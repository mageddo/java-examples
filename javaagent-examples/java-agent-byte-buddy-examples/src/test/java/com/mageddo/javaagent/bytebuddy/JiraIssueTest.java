package com.mageddo.javaagent.bytebuddy;

import net.bytebuddy.agent.ByteBuddyAgent;

import org.junit.jupiter.api.Test;

import java.lang.instrument.Instrumentation;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class JiraIssueTest {

  @Test
  void mustInstallToCurrentVmAndFINDOrangeFruit(){
    // arrange
    new JiraIssue("");
    final Instrumentation instrumentation = ByteBuddyAgent.install();
    // act
    final long found = Arrays
        .stream(instrumentation.getAllLoadedClasses())
        .filter(it -> it.getName().equals(JiraIssue.class.getName()))
        .count()
        ;

    // assert
    assertEquals(1, found);
  }
}
