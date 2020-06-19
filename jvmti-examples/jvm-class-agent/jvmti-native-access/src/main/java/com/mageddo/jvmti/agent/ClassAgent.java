package com.mageddo.jvmti.agent;

import com.mageddo.jvmti.ClassId;
import com.mageddo.jvmti.JvmtiNativeLibraryFinder;
import com.mageddo.jvmti.NativeLoader;
import com.mageddo.jvmti.Server;
import com.mageddo.jvmti.classdelegate.LocalClassInstanceService;
import com.mageddo.jvmti.classdelegate.scanning.ReferenceFilterFactory;
import com.mageddo.jvmti.poc.JiraIssue;
import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.Instrumentation;

@Slf4j
public class ClassAgent {

  public static void premain(String agentArgs, Instrumentation instrumentation) {
    log.info("pre main!!!!!!!!!!!!!!!!!!");
  }

  public static void agentmain(String agentArgs, Instrumentation instrumentation) {

    log.info("agent main started!!!");
//    new NativeLoader(new JvmtiNativeLibraryFinder()).load();
//    log.info("dll loaded!");
    final LocalClassInstanceService classInstanceService = new LocalClassInstanceService(
      new ReferenceFilterFactory()
    );
//    final int instances = classInstanceService.scanInstances(ClassId.of("ben"));
    final int instances = classInstanceService.scanInstances(ClassId.of(JiraIssue.class));
    log.info("{} instances", instances);
//
    Server.start();
  }
}