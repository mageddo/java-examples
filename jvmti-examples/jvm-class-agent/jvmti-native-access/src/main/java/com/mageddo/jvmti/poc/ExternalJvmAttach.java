package com.mageddo.jvmti.poc;

import com.mageddo.jvmti.CurrentJarLoader;
import com.mageddo.jvmti.JvmtiNativeLibraryFinder;
import com.mageddo.jvmti.NativeLoader;
import com.sun.tools.attach.AgentLoadException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
public class ExternalJvmAttach {

  public static void main(String[] args) {
    log.debug("starting....");
    final int pid = toInteger(args[0]);
    try {
      new NativeLoader(new JvmtiNativeLibraryFinder()).load(pid);
    } catch (Exception e){
     if(!isFalsePositiveException(e)){
       throw e;
     } else {
       log.info("status=false-positive, msg={}", e.getMessage());
     }
    }
    try {
      new CurrentJarLoader().load(pid);
    } catch (Exception e){
      if(!isFalsePositiveException(e)){
        throw e;
      } else {
        log.info("status=false-positive, msg={}", e.getMessage());
      }
    }
  }

  private static int toInteger(String arg) {
    if(arg.startsWith("0x")){
      return Integer.parseInt(arg.substring(2), 16);
    }
    return Integer.parseInt(arg);
  }

  static boolean isFalsePositiveException(Exception e){
    return ExceptionUtils.getStackTrace(e).contains("AgentLoadException: 0");
  }
}