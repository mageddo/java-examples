package com.mageddo.javaagent.bytebuddy;

import java.lang.management.ManagementFactory;

public class VmUtils {
  public static int getCurrentPid() {
    return Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
  }
}
