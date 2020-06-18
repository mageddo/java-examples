package com.mageddo.jvmti.classdelegate;

import com.mageddo.jvmti.JvmtiClass;
import com.mageddo.jvmti.classdelegate.scanning.InstanceFilter;
import com.mageddo.jvmti.classdelegate.scanning.ReferenceFilterFactory;
import com.mageddo.jvmti.entrypoint.vo.ArgsReq;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Singleton
public class ClassInstanceService {

  private final ReferenceFilterFactory referenceFilterFactory;
  private List<ObjectReference> instances;
  private Map<InstanceId, ObjectReference> instanceStore;

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

  public ObjectReference invoke(InstanceId id, String name, List<ArgsReq> argsReq) {
    final ObjectReference objectReference = this.getReference(id);
    final Object[] args = argsReq
      .stream()
      .map(it -> {
        final Object arg = it.toArg();
        if(arg instanceof InstanceId){
          return this.getReference((InstanceId) arg);
        }
        return arg;
      })
      .toArray();
    return objectReference.invoke(name, args);
  }

  ObjectReference getReference(InstanceId id) {
    return this.instanceStore.get(id);
  }
}
