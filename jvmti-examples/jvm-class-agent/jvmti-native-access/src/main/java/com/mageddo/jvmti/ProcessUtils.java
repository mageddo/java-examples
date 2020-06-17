package com.mageddo.jvmti;

import lombok.experimental.UtilityClass;

import java.lang.management.ManagementFactory;

@UtilityClass
public class ProcessUtils {
  public static int getCurrentPid() {
    return Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
  }
}
