package com.mageddo.jvmti;

import org.apache.commons.lang3.SystemUtils;

/**
 * Discover the OS and ARCH respective jvmti native library
 */
public class JvmtiNativeLibraryFinder {

  public NativeLibrary find(){
    if(SystemUtils.IS_OS_LINUX && this.isAmd64Arch()){
      return new NativeLibrary("/linux-x64/jvmti.so");
    } else if(SystemUtils.IS_OS_WINDOWS && this.isAmd64Arch()){
      return new NativeLibrary("/windows-x64/jvmti.dll");
    } else if(SystemUtils.IS_OS_WINDOWS && this.isX86Arch()){
      return new NativeLibrary("/windows-x86/jvmti.dll");
    }
    throw new UnsupportedOperationException(String.format(
      "Os not implemented yet: %s (%s)",
      SystemUtils.OS_NAME,
      SystemUtils.OS_ARCH
    ));
  }

  private boolean isX86Arch() {
    return SystemUtils.OS_ARCH.equals("x86");
  }

  private boolean isAmd64Arch() {
    return SystemUtils.OS_ARCH.equals("amd64");
  }
}
