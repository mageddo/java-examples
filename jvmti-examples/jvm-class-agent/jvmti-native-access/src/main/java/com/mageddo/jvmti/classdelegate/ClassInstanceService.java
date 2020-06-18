package com.mageddo.jvmti.classdelegate;

import com.mageddo.jvmti.JvmtiClass;
import com.mageddo.jvmti.classdelegate.scanning.InstanceFilter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassInstanceService {

  private List<ObjectReference> instances;

  public void scan(Class<?> clazz) {
    this.instances = Stream
      .of(JvmtiClass.getClassInstances(clazz))
      .map(ObjectReference::new)
      .collect(Collectors.toList());
  }

  public void filter(InstanceFilter filter){

  }
}
