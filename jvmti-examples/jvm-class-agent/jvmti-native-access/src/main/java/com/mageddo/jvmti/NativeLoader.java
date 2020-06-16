package com.mageddo.jvmti;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.agent.ByteBuddyAgent;

@RequiredArgsConstructor
public class NativeLoader {

  private final JvmtiNativeLibraryFinder jvmtiNativeLibraryFinder;

  public void load(int pid) {
    ByteBuddyAgent.attachNative(
      this.jvmtiNativeLibraryFinder
        .find()
        .installAtTempPath()
        .toFile(),
      String.valueOf(pid)
    );
  }

}
