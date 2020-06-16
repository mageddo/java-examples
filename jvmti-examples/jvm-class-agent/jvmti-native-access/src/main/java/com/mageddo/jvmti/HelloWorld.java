package com.mageddo.jvmti;

public class HelloWorld {

  public static void main(String[] args) {
    final int pid = Integer.parseInt(args[0]);
    new NativeLoader(new JvmtiNativeLibraryFinder()).load(pid);
    new CurrentJarLoader().load(pid);
  }
}