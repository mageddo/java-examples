package com.mageddo.jvmti;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(of = {"name"})
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class FieldId {

//  /**
//   * probably not necessary
//   */
//  @Deprecated
//  ClassId classId;

  String name;

  public static FieldId of(ClassId classId, String name){
    return FieldId
      .builder()
//      .classId(classId)
      .name(name)
      .build();
  }
}
