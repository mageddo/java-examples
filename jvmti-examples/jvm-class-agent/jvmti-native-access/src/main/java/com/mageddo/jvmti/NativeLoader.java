package com.mageddo.jvmti;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.agent.ByteBuddyAgent;

import java.io.File;

@RequiredArgsConstructor
public class NativeLoader {

  private final JvmtiNativeLibraryFinder jvmtiNativeLibraryFinder;

  public void load() {
    System.load(String.valueOf(this.getJvmtiLibrary()));
  }

  /**
   * Load the jvmti library into a specified jvm
   */
  public void load(int pid) {
    ByteBuddyAgent.attachNative(this.getJvmtiLibrary(), String.valueOf(pid));
  }

  private File getJvmtiLibrary() {
    return this.jvmtiNativeLibraryFinder
      .find()
      .installAtTempPath()
      .toFile();
  }

}
