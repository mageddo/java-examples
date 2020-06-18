package com.mageddo.jvmti.classdelegate;

import lombok.SneakyThrows;
import lombok.Value;

@Value
public class ObjectReference {

  private final Object instance;

  @SneakyThrows
  public ObjectReference getFieldValue(String name) {
    final Class<?> clazz = this.getInstanceClass();
    return of(FieldReflections
      .getField(clazz, name)
      .get(this.instance)
    );
  }

  public ObjectReference invoke(String name, Object ... args) {
    return of(MethodReflections.invoke(this.instance, name, args));
  }

  public Class<?> getInstanceClass() {
    return this.instance.getClass();
  }

  public static ObjectReference of(Object instance) {
    if(instance == null){
      return null;
    }
    return new ObjectReference(instance);
  }

}
