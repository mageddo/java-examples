package com.mageddo.jvmti.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.jvmti.ClassId;
import com.mageddo.jvmti.ClassInstanceService;
import com.mageddo.jvmti.InstanceValue;
import com.mageddo.jvmti.classdelegate.LocalClassInstanceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.metzweb.tinyserver.Request;
import net.metzweb.tinyserver.Response;
import net.metzweb.tinyserver.TinyServer;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Slf4j
@Singleton
public class ClassInstancesRetrieverResource implements Response {

  private final ObjectMapper objectMapper;
  private final ClassInstanceService classInstanceService;

  @Inject
  public ClassInstancesRetrieverResource(
    TinyServer tinyServer,
    ObjectMapper objectMapper,
    LocalClassInstanceService classInstanceService
  ) {
    this.objectMapper = objectMapper;
    this.classInstanceService = classInstanceService;
    tinyServer.get("/class-instances", this);
  }

  @Override
  @SneakyThrows
  public void callback(Request request) {
    try {
      final String className = request.param("class");
      final List<InstanceValue> found = this.classInstanceService.scanAndGetValues(ClassId.of(className.trim()));
      request.write(this.objectMapper.writeValueAsString(found));
    } catch (Exception e){
      log.warn("status=can't-retrieve-class-instances, class={}, e", request.getData(), e);
      request.write(String.format(
        "can't-retrieve-class-instances=%s, msg=%s",
        request.getData(),
        ExceptionUtils.getRootCauseMessage(e)
      ));
    }
  }
}
