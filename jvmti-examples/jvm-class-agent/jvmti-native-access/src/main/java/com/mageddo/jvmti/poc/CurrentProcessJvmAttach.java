package com.mageddo.jvmti.poc;

import com.mageddo.jvmti.ClassDefinition;
import com.mageddo.jvmti.JvmtiClass;

import java.util.stream.Stream;

public class CurrentProcessJvmAttach {
  public static void main(String[] args) {
//    new NativeLoader(new JvmtiNativeLibraryFinder()).load();

    new JiraIssue("xxx");
    System.out.printf("instances: %d%n", JvmtiClass.countInstances(JiraIssue.class));
//    System.out.printf("classes: %s%n", JvmtiClass.findLoadedClasses());
    final Class[] classes = JvmtiClass.findLoadedClasses();
    for (Class jclass : classes) {
      System.out.println(jclass.getSimpleName());
    }
    System.out.println(classes.length);

    System.out.println("findClassMethods");
    final ClassDefinition classDefinition = new ClassDefinition();
    final Object result = JvmtiClass.findClassMethods(JiraIssue.class, ClassDefinition.class, classDefinition);
    System.out.println(classDefinition);
//    System.out.println("result: "  + );
    System.out.println(">>>>>>>>>");
    Stream.of(result.getClass().getFields()).forEach(it -> System.out.println(it.getName()));
    System.out.println("<<<<<<<<<<<<<");
  }
}
