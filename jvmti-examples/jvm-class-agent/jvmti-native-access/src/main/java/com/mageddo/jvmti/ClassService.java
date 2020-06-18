package com.mageddo.jvmti;

import java.util.List;

public interface ClassService {

  List<ClassId> getLoadedClasses();

  List<FieldId> getFields(ClassId classId);

  List<MethodId> getMethods(ClassId classId);

  FieldId getField(ClassId classId, String name);

  FieldId getMethod(ClassId classId, String name, Class... argsTypes);

}
