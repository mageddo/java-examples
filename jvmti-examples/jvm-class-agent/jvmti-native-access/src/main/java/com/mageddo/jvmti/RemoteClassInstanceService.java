package com.mageddo.jvmti;

import com.mageddo.jvmti.classdelegate.InstanceId;
import com.mageddo.jvmti.classdelegate.scanning.InstanceFilter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.List;

@Slf4j
@Singleton
public class RemoteClassInstanceService implements ClassInstanceService {

  @Override
  public InstanceValue getFieldValue(InstanceId id, FieldId fieldId) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setFieldValue(InstanceId id, FieldId fieldId, InstanceValue value) {
    log.info("status=setFieldValue, id={}, field={}, instance={}", id, fieldId, value);
  }

  @Override
  public InstanceValue methodInvoke(InstanceId id, String name, List<InstanceValue> args) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int filter(InstanceFilter filter) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int scanInstances(ClassId classId) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<InstanceValue> scanAndGetValues(ClassId classId) {
    throw new UnsupportedOperationException();
  }
}
