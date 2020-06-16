package com.mageddo.jvmti.agents;

import com.mageddo.jvmti.JiraIssue;
import com.mageddo.jvmti.JvmtiClass;

import java.lang.instrument.Instrumentation;
import java.nio.file.Paths;

public class ClassAgent {
  public static void premain(String agentArgs, Instrumentation instrumentation) {
    System.out.println("pre main!!!!!!!!!!!!!!!!!!");
  }

  public static void agentmain(String agentArgs, Instrumentation instrumentation) {
//    final var agentDllPath = Paths.get("H:\\jogos-com-backup\\java-examples\\jvmti-examples\\jvm-class-agent\\build\\libs\\jvmtiInstanceCounter\\shared\\jvmtiInstanceCounter.dll").toFile();

//    System.out.println("loading: " + agentDllPath);
//    System.loadLibrary(String.valueOf(agentDllPath));
    System.out.println("agent main v2!!!!!!!!!!");
    System.out.printf("%d instances!!!", JvmtiClass.countInstances(JiraIssue.class));
  }
}