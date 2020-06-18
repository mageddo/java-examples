package com.mageddo.jvmti.agents.entrypoint;

import com.mageddo.jvmti.JvmtiClass;
import lombok.SneakyThrows;
import net.metzweb.tinyserver.Request;
import net.metzweb.tinyserver.Response;
import net.metzweb.tinyserver.TinyServer;

public class ClassInstancesFinderResource implements Response {

  private Object[] classInstances;

  public ClassInstancesFinderResource(TinyServer tinyServer) {
    tinyServer.post("/class-instances/find", this);
  }

  @Override
  @SneakyThrows
  public void callback(Request request) {
    final String clazzName = request.getData();
    this.classInstances = JvmtiClass.getClassInstances(Class.forName(clazzName));
  }
}
