package com.mageddo.jvmti.classdelegate;

import com.mageddo.jvmti.ClassId;
import com.mageddo.jvmti.ClassInstanceService;
import com.mageddo.jvmti.FieldId;
import com.mageddo.jvmti.InstanceValue;
import com.mageddo.jvmti.JvmtiClass;
import com.mageddo.jvmti.classdelegate.scanning.InstanceFilter;
import com.mageddo.jvmti.classdelegate.scanning.ReferenceFilterFactory;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Singleton
public class LocalClassInstanceService implements ClassInstanceService {

  private final ReferenceFilterFactory referenceFilterFactory;
  private Map<InstanceId, ObjectReference> instanceStore;

  @Inject
  public LocalClassInstanceService(ReferenceFilterFactory referenceFilterFactory) {
    this.referenceFilterFactory = referenceFilterFactory;
  }

  @Override
  public InstanceValue getFieldValue(InstanceId id, FieldId fieldId) {
    return this.getReference(id)
      .getFieldValue(fieldId.getName())
      .toInstanceValue()
      ;
  }

  @Override
  public void setFieldValue(InstanceId id, FieldId fieldId, InstanceValue value) {
    this.getReference(id).setFieldValue(fieldId.getName(), this.toArg(value));
  }

  @Override
  public InstanceValue methodInvoke(InstanceId id, String name, List<InstanceValue> args) {
    final ObjectReference objectReference = this.getReference(id);
    final Object[] parsedArgs = args
      .stream()
      .map(this::toArg)
      .toArray();
    final ObjectReference reference = objectReference.invoke(name, parsedArgs);
    this.putToStore(reference);
    return reference.toInstanceValue();
  }

  public int filter(InstanceFilter filter) {
    return this.referenceFilterFactory.filter(this.instanceStore.values(), filter);
  }

  @Override
  public int scanInstances(ClassId classId) {
    this.instanceStore = new HashMap<>();
    Stream
      .of(JvmtiClass.getClassInstances(classId.toClass()))
      .forEach(it -> {
        final ObjectReference reference = new ObjectReference(it);
        this.instanceStore.put(reference.id(), reference);
      });
    log.info("status=scanned, instances={}", this.instanceStore.size());
    return this.instanceStore.size();
  }

  @Override
  public List<InstanceValue> scanAndGetValues(ClassId classId) {
    this.scanInstances(classId);
    return this.instanceStore
      .values()
      .stream()
      .map(ObjectReference::toInstanceValue)
      .collect(Collectors.toList())
      ;
  }

  ObjectReference getReference(InstanceId id) {
    return this.instanceStore.get(id);
  }

  void putToStore(ObjectReference reference) {
    this.instanceStore.put(reference.id(), reference);
  }

  Object toArg(InstanceValue value) {
    final Object o = value.toArg();
    if(o.getClass() == InstanceId.class){
      return this.instanceStore.get(o);
    }
    return o;
  }
}
