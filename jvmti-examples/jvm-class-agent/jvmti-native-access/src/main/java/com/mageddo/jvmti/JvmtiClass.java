package com.mageddo.jvmti;

public class JvmtiClass {
  public static native int countInstances(Class jClass);
  public static native int findLoadedClasses();
}
