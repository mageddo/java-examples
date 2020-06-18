package com.mageddo.jvmti.agents.entrypoint;

import com.mageddo.jvmti.JvmtiClass;
import lombok.SneakyThrows;
import net.metzweb.tinyserver.Request;
import net.metzweb.tinyserver.Response;

public class ObjectReferenceResource implements Response {

  private Object[] classInstances;

  @Override
  @SneakyThrows
  public void callback(Request request) {
    final String clazzName = request.getData();
    this.classInstances = JvmtiClass.getClassInstances(Class.forName(clazzName));

  }
}
