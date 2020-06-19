package com.mageddo.jvmti.entrypoint.vo;

import com.mageddo.jvmti.FieldId;
import com.mageddo.jvmti.InstanceValue;
import com.mageddo.jvmti.classdelegate.InstanceId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstanceFieldValueSetReq {

  InstanceId objectId;

  FieldId fieldId;

  InstanceValue value;
}
