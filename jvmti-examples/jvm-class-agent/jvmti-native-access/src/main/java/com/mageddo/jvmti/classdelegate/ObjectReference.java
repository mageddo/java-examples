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

  @SneakyThrows
  public ObjectReference setFieldValue(String name, Object value){
    FieldReflections
      .getField(this.getInstanceClass(), name)
      .set(this.instance, value)
    ;
    return this;
  }

  public ObjectReference invoke(String name, Object ... args) {
    return of(MethodReflections.invoke(this.instance, name, args));
  }

  public Class<?> getInstanceClass() {
    return this.instance.getClass();
  }

  public InstanceId id(){
    return InstanceId.of(this.instance);
  }

  public int hashCode(){
    return this.instance.hashCode();
  }

  public static ObjectReference of(Object instance) {
    if(instance == null){
      return null;
    }
    return new ObjectReference(instance);
  }

  public String asText() {
    return String.valueOf(this.instance);
  }

  public Double asDouble() {
    return ((Number) this.instance).doubleValue();
  }
}
