package com.mageddo.jvmti.classdelegate;

import com.mageddo.jvmti.JvmtiClass;
import com.mageddo.jvmti.classdelegate.scanning.InstanceFilter;
import com.mageddo.jvmti.classdelegate.scanning.ReferenceFilterFactory;
import lombok.RequiredArgsConstructor;

import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
@RequiredArgsConstructor
public class ClassInstanceService {

  private final ReferenceFilterFactory referenceFilterFactory;
  private List<ObjectReference> instances;

  public void scan(Class<?> clazz) {
    this.instances = Stream
      .of(JvmtiClass.getClassInstances(clazz))
      .map(ObjectReference::new)
      .collect(Collectors.toList());
  }

  public int filter(InstanceFilter filter){
    return this.referenceFilterFactory.filter(this.instances, filter);
  }
}
