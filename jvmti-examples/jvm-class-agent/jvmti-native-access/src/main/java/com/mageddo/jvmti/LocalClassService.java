package com.mageddo.jvmti;

import com.mageddo.jvmti.classdelegate.FieldReflections;
import com.mageddo.jvmti.classdelegate.MethodReflections;

import javax.inject.Singleton;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class LocalClassService implements ClassService {

  @Override
  public List<ClassId> getLoadedClasses() {
    return Stream
      .of(JvmtiClass.findLoadedClasses())
      .map(ClassId::of)
      .collect(Collectors.toList());
  }

  @Override
  public List<FieldId> getFields(ClassId classId) {
    return FieldReflections.getFields(classId.toClass())
      .stream()
      .map(this::fieldToFieldId)
      .collect(Collectors.toList());
  }

  @Override
  public List<MethodId> getMethods(ClassId classId) {
    return MethodReflections
      .getMethods(classId.toClass())
      .stream()
      .map(this::methodToMethodId)
      .collect(Collectors.toList())
      ;
  }

  @Override
  public FieldId getField(ClassId classId, String name) {
    return FieldId
      .builder()
      .classId(classId)
      .name(FieldReflections.getField(classId.toClass(), name).getName())
      .build()
      ;
  }

  @Override
  public MethodId getMethod(ClassId classId, String name, ClassId... argsTypes) {
    return MethodId
      .builder()
      .name(MethodReflections.getMethod(classId.toClass(), name, this.toClasses(argsTypes)).getName())
      .build();
  }

  FieldId fieldToFieldId(Field field) {
    return FieldId
      .builder()
      .classId(ClassId.of(field.getDeclaringClass()))
      .name(field.getName())
      .build();
  }

  MethodId methodToMethodId(Method method) {
    return MethodId
      .builder()
      .classId(ClassId.of(method.getDeclaringClass()))
      .name(method.getName())
      .argsTypes(ClassId.of(method.getParameterTypes()))
      .build()
      ;
  }

  Class[] toClasses(ClassId[] argsTypes) {
    return Stream
      .of(argsTypes)
      .map(ClassId::toClass)
      .toArray(Class[]::new)
      ;
  }
}
