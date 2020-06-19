package com.mageddo.jvmti;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.security.auth.callback.Callback;
import java.util.function.Function;

@Slf4j
public class ExternalJvmAttach {

  /**
   * @param pid in hexadecimal format ex: 0xFF
   */
  public static void attach(String pid) {
    attach(toInteger(pid));
  }

  public static void attach(int pid) {
    attach(() -> {
      new NativeLoader(new JvmtiNativeLibraryFinder()).load(pid);
    });
    attach(() -> {
      new CurrentJarLoader().load(pid);
    });
  }

  static void attach(Callback callback) {
    try {
      callback.run();
    } catch (Exception e) {
      if (!isFalsePositiveException(e)) {
        throw e;
      } else {
        log.info("status=false-positive, msg={}", e.getMessage());
      }
    }
  }

  static int toInteger(String arg) {
    if (arg.startsWith("0x")) {
      return Integer.parseInt(arg.substring(2), 16);
    }
    return Integer.parseInt(arg);
  }

  static boolean isFalsePositiveException(Exception e) {
    return ExceptionUtils.getStackTrace(e).contains("AgentLoadException: 0");
  }

  interface Callback {
    void run();
  }
}