package com.mageddo.jvmti.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.jvmti.ClassId;
import com.mageddo.jvmti.ClassInstanceService;
import com.mageddo.jvmti.InstanceValue;
import com.mageddo.jvmti.classdelegate.LocalClassInstanceService;
import com.mageddo.jvmti.entrypoint.vo.InstanceFieldValueSetReq;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.metzweb.tinyserver.Request;
import net.metzweb.tinyserver.Response;
import net.metzweb.tinyserver.TinyServer;
import net.metzweb.tinyserver.response.StatusCode;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Slf4j
@Singleton
public class ClassInstanceFieldSetterResource implements Response {

  private final ObjectMapper objectMapper;
  private final ClassInstanceService classInstanceService;

  @Inject
  public ClassInstanceFieldSetterResource(
    TinyServer tinyServer,
    ObjectMapper objectMapper,
    LocalClassInstanceService classInstanceService
  ) {
    this.objectMapper = objectMapper;
    this.classInstanceService = classInstanceService;
    tinyServer.post("/class-instances/fields/set-value", this);
  }

  @Override
  @SneakyThrows
  public void callback(Request request) {
    try {
      log.debug("status=before-set, data={}", request.getData());
      final InstanceFieldValueSetReq instanceFieldValueSetReq = this.objectMapper.readValue(
        request.getData(),
        InstanceFieldValueSetReq.class
      );
      this.classInstanceService.setFieldValue(
        instanceFieldValueSetReq.getObjectId(),
        instanceFieldValueSetReq.getFieldId(),
        instanceFieldValueSetReq.getValue()
      );
      log.debug("status=after-set, data={}", request.getData());
      request.write("value changed!");
      log.debug("status=success, data={}", request.getData());
    } catch (Exception e){
      final String msg = String.format("status=can't change field value: %s%n%s", request.getData(), e.getMessage());
      log.warn(msg, e);
      request.write().write(StatusCode.BAD_REQUEST, msg);
    }
  }
}
