package com.mageddo.jvmti;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(of = {"name", "classId"})
public class FieldId {

  /**
   * probably not necessary
   */
  @Deprecated
  ClassId classId;

  String name;

  public static FieldId of(ClassId classId, String name){
    return FieldId
      .builder()
      .classId(classId)
      .name(name)
      .build();
  }
}
