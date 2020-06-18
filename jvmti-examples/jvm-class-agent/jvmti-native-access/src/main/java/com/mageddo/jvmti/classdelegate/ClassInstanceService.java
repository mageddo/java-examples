package com.mageddo.jvmti.classdelegate;

import com.mageddo.jvmti.JvmtiClass;
import com.mageddo.jvmti.classdelegate.scanning.InstanceFilter;
import com.mageddo.jvmti.classdelegate.scanning.ReferenceFilterFactory;
import com.mageddo.jvmti.entrypoint.vo.ArgsReq;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Singleton
public class ClassInstanceService {

  private final ReferenceFilterFactory referenceFilterFactory;
  private List<ObjectReference> instances;

  @Inject
  public ClassInstanceService(ReferenceFilterFactory referenceFilterFactory) {
    this.referenceFilterFactory = referenceFilterFactory;
  }

  public int scan(Class<?> clazz) {
    this.instances = Stream
      .of(JvmtiClass.getClassInstances(clazz))
      .map(ObjectReference::new)
      .collect(Collectors.toList());
    log.info("status=scanned, instances={}", this.instances.size());
    return this.instances.size();
  }

  public int filter(InstanceFilter filter) {
    return this.referenceFilterFactory.filter(this.instances, filter);
  }

  public void invoke(int instanceHashCode, String name, List<ArgsReq> argsReq) {
    Object[] args = argsReq
      .stream()
      .map(it -> {
        // todo
        // converter argreq para objeto primitivo ou ObjectReference
        throw new UnsupportedOperationException();
      })
      .toArray();

  }
}
