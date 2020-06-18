package com.mageddo.jvmti.agents.entrypoint;

import com.mageddo.jvmti.classdelegate.ClassInstanceService;
import lombok.SneakyThrows;
import net.metzweb.tinyserver.Request;
import net.metzweb.tinyserver.Response;
import net.metzweb.tinyserver.TinyServer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ClassInstancesFinderResource implements Response {

  private final ClassInstanceService classInstanceService;

  @Inject
  public ClassInstancesFinderResource(TinyServer tinyServer, ClassInstanceService classInstanceService) {
    this.classInstanceService = classInstanceService;
    tinyServer.post("/class-instances/find", this);
    System.out.println(this.getClass().getSimpleName() + tinyServer);
  }

  @Override
  @SneakyThrows
  public void callback(Request request) {
    final String clazzName = request.getData();
    final int found = this.classInstanceService.scan(Class.forName(clazzName.trim()));
    request.write(String.format("%d instances found", found));
  }
}
