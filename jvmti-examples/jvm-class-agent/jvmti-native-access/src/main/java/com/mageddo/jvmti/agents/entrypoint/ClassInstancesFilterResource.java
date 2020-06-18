package com.mageddo.jvmti.agents.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.jvmti.agents.entrypoint.vo.InstanceFilterReq;
import com.mageddo.jvmti.classdelegate.ClassInstanceService;
import lombok.SneakyThrows;
import net.metzweb.tinyserver.Request;
import net.metzweb.tinyserver.Response;
import net.metzweb.tinyserver.TinyServer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ClassInstancesFilterResource implements Response {

  private final ClassInstanceService classInstanceService;
  private final ObjectMapper objectMapper;

  @Inject
  public ClassInstancesFilterResource(
    TinyServer tinyServer,
    ClassInstanceService classInstanceService,
    ObjectMapper objectMapper
  ) {
    this.classInstanceService = classInstanceService;
    this.objectMapper = objectMapper;
    tinyServer.post("/class-instances/filter", this);
    System.out.println(this.getClass().getSimpleName() + tinyServer);
  }

  @Override
  @SneakyThrows
  public void callback(Request request) {
    final int removed = this.classInstanceService.filter(
      this.objectMapper
        .readValue(request.getData(), InstanceFilterReq.class)
        .toInstanceFilter()
    );
    request.write("removed instances after filtering: " + removed);
  }
}
