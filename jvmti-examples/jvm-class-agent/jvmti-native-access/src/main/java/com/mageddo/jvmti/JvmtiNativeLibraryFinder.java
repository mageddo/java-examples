package com.mageddo.jvmti;

import org.apache.commons.lang3.SystemUtils;

/**
 * Discover the OS and ARCH respective jvmti native library
 */
public class JvmtiNativeLibraryFinder {

  public NativeLibrary find(){
    if(SystemUtils.IS_OS_LINUX && SystemUtils.OS_ARCH.equals("amd64")){
      return new NativeLibrary("/linux-x64/jvmti.so");
    }
    throw new UnsupportedOperationException(String.format(
      "Os not implemented yet: %s (%s)",
      SystemUtils.OS_NAME,
      SystemUtils.OS_ARCH
    ));
  }
}
