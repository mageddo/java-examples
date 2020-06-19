package com.mageddo.jvmti.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.jvmti.ClassInstanceService;
import com.mageddo.jvmti.classdelegate.InstanceId;
import com.mageddo.jvmti.classdelegate.LocalClassInstanceService;
import com.mageddo.jvmti.entrypoint.vo.ArgsReq;
import com.mageddo.jvmti.entrypoint.vo.MethodInvokeReq;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.metzweb.tinyserver.Request;
import net.metzweb.tinyserver.Response;
import net.metzweb.tinyserver.TinyServer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class ClassInstanceMethodInvokeResource implements Response {

  private final ClassInstanceService classInstance;
  private final ObjectMapper objectMapper;

  @Inject
  public ClassInstanceMethodInvokeResource(
    TinyServer tinyServer,
    LocalClassInstanceService classInstance,
    ObjectMapper objectMapper
  ) {
    this.classInstance = classInstance;
    this.objectMapper = objectMapper;
    tinyServer.post("/class-instances/method-invoke", this);
  }

  @Override
  @SneakyThrows
  public void callback(Request request) {
    try {
      final MethodInvokeReq methodInvoke = this.objectMapper
        .readValue(request.getData(), MethodInvokeReq.class);
      this.classInstance.methodInvoke(
        InstanceId.of(methodInvoke.getInstanceId()),
        methodInvoke.getName(),
        ArgsReq.toInstanceValues(methodInvoke.getArgs())
      );
      request.write("");
    } catch (Exception e){
      log.warn("status=can't-invoke, req={}", request.getData(), e);
    }
  }
}
