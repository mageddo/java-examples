package com.mageddo.jvmti;

public class JvmtiClass {
  public static native int countInstances(Class jClass);
  public static native Class[] findLoadedClasses();
  public static native Object findClassMethods(Class jclass,
    Class<?> classDefinitionClass,
    ClassDefinition classDefinition
  );
}
