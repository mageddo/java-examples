package com.mageddo.jvmti;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloWorld {

  public static void main(String[] args) {
    log.debug("starting....");
    final int pid = Integer.parseInt(args[0]);
    new NativeLoader(new JvmtiNativeLibraryFinder()).load(pid);
    new CurrentJarLoader().load(pid);
  }
}