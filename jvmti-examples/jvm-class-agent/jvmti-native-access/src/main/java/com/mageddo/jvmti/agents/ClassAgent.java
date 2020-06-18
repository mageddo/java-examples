package com.mageddo.jvmti.agents;

import com.mageddo.jvmti.JvmtiClass;
import com.mageddo.jvmti.agents.entrypoint.ClassInstancesFinderResource;
import com.mageddo.jvmti.dependencies.DependencyInjector;
import com.mageddo.jvmti.poc.JiraIssue;
import net.metzweb.tinyserver.TinyServer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.instrument.Instrumentation;

public class ClassAgent {

  private static TinyServer tinyServer;

  public static void premain(String agentArgs, Instrumentation instrumentation) {
    System.out.println("pre main!!!!!!!!!!!!!!!!!!");
  }

  public static void agentmain(String agentArgs, Instrumentation instrumentation) {
//    final var agentDllPath = Paths.get("H:\\jogos-com-backup\\java-examples\\jvmti-examples\\jvm-class-agent\\build\\libs\\jvmtiInstanceCounter\\shared\\jvmtiInstanceCounter.dll").toFile();

//    System.out.println("loading: " + agentDllPath);
//    System.loadLibrary(String.valueOf(agentDllPath));
    System.out.println("agent main v2!!!!!!!!!!");
    System.out.printf("%d instances!!!", JvmtiClass.countInstances(JiraIssue.class));

    setupServer();

  }

  private static void setupServer() {
    if (tinyServer != null) {
      System.out.println("already started");
      return;
    }
    System.out.println("starting tiny server");
    final DependencyInjector dependencyInjector = new DependencyInjector();
    tinyServer = dependencyInjector.get(TinyServer.class);
    new ClassInstancesFinderResource(tinyServer);
    new ClassInstancesFilterResource(tinyServer, classInstanceService, objectMapper);
    tinyServer.post("/instances", request -> {
      try {
        System.out.println("listing instances");
        final int instances = JvmtiClass.countInstances(Class.forName(request.getData()));
        request.write(String.format("%d instances", instances));
      } catch (Exception e) {
        final StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        request.write("Fatal: " + stringWriter.toString());
      }
    });
    tinyServer.start();

  }
}