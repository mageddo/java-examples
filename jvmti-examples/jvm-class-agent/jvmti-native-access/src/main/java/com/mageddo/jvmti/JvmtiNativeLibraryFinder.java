package com.mageddo.jvmti;

import org.apache.commons.lang3.SystemUtils;

public class JvmtiNativeLibraryFinder {

  public NativeLibrary find(){
    if(SystemUtils.IS_OS_LINUX && SystemUtils.OS_ARCH.equals("x64")){
      return new NativeLibrary("/linux-x64/jvmti.so");
    }
    throw new UnsupportedOperationException("Os not implemented yet");
  }
}
