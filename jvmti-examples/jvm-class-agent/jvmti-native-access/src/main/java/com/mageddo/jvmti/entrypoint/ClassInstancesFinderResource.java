package com.mageddo.jvmti.entrypoint;

import com.mageddo.jvmti.ClassId;
import com.mageddo.jvmti.ClassInstanceService;
import com.mageddo.jvmti.classdelegate.LocalClassInstanceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.metzweb.tinyserver.Request;
import net.metzweb.tinyserver.Response;
import net.metzweb.tinyserver.TinyServer;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class ClassInstancesFinderResource implements Response {

  private final ClassInstanceService classInstanceService;

  @Inject
  public ClassInstancesFinderResource(TinyServer tinyServer, LocalClassInstanceService classInstanceService) {
    this.classInstanceService = classInstanceService;
    tinyServer.post("/class-instances/find", this);
  }

  @Override
  @SneakyThrows
  public void callback(Request request) {
    try {
      final String clazzName = request.getData();
      final int found = this.classInstanceService.scanInstances(ClassId.of(clazzName.trim()));
      request.write(String.format("%d instances found", found));
    } catch (Exception e){
      log.warn("status=can't-find-class, class={}, e", request.getData(), e);
      request.write(String.format(
        "couldn't find class=%s, msg=%s",
        request.getData(),
        ExceptionUtils.getRootCauseMessage(e)
      ));
    }
  }
}
