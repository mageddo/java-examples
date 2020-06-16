package com.mageddo.jvmti;

import net.bytebuddy.agent.ByteBuddyAgent;

import java.nio.file.Paths;

public class HelloWorld {

  public static void main(String[] args) {

    final var pid = args[0];
    final var agentDllPath = Paths.get("/home/typer/dev/projects/java-examples/jvmti-examples/jvm-class-agent/build/libs/jvmtiInstanceCounter/shared/libjvmtiInstanceCounter.so").toFile();
    ByteBuddyAgent.attachNative(agentDllPath, pid);

    final var agentJarPath = Paths.get("/home/typer/dev/projects/java-examples/jvmti-examples/jvm-class-agent/build/libs/jvmti-jvm-class-agent-all.jar").toFile();
    ByteBuddyAgent.attach(agentJarPath, pid);

//    FruitInstanceMaker.makeInstances(new Random().nextInt(100));
//    System.out.println("instances: " + JvmClassAgent.countInstances(JiraIssue.class));
  }


}