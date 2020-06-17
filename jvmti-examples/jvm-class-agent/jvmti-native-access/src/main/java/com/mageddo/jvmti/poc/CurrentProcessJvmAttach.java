package com.mageddo.jvmti.poc;

import com.mageddo.jvmti.JvmtiClass;

public class CurrentProcessJvmAttach {
  public static void main(String[] args) {
//    new NativeLoader(new JvmtiNativeLibraryFinder()).load();

    new JiraIssue("xxx");
    System.out.printf("instances: %d%n", JvmtiClass.countInstances(JiraIssue.class));
    System.out.printf("instances: %d%n", JvmtiClass.findLoadedClasses());

  }
}
