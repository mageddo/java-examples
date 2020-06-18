package com.mageddo.jvmti.agents.entrypoint;

import com.mageddo.jvmti.classdelegate.ClassInstanceService;
import lombok.SneakyThrows;
import net.metzweb.tinyserver.Request;
import net.metzweb.tinyserver.Response;
import net.metzweb.tinyserver.TinyServer;

import javax.inject.Singleton;

@Singleton
public class ClassInstancesFinderResource implements Response {

  private final ClassInstanceService classInstanceService;

  public ClassInstancesFinderResource(TinyServer tinyServer, ClassInstanceService classInstanceService) {
    this.classInstanceService = classInstanceService;
    tinyServer.post("/class-instances/find", this);
  }

  @Override
  @SneakyThrows
  public void callback(Request request) {
    final String clazzName = request.getData();
    this.classInstanceService.scan(Class.forName(clazzName));
  }
}
