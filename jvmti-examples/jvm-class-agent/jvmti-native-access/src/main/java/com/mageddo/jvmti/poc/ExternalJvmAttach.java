package com.mageddo.jvmti.poc;

import com.mageddo.jvmti.CurrentJarLoader;
import com.mageddo.jvmti.JvmtiNativeLibraryFinder;
import com.mageddo.jvmti.NativeLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExternalJvmAttach {

  public static void main(String[] args) {
    log.debug("starting....");
    final int pid = Integer.parseInt(args[0]);
    new NativeLoader(new JvmtiNativeLibraryFinder()).load(pid);
    new CurrentJarLoader().load(pid);
  }
}