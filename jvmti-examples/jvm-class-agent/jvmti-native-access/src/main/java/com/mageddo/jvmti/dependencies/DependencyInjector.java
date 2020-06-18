package com.mageddo.jvmti.dependencies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.jvmti.agents.ClassInstancesFilterResource;
import com.mageddo.jvmti.agents.entrypoint.ClassInstancesFinderResource;
import com.mageddo.jvmti.classdelegate.ClassInstanceService;
import net.metzweb.tinyserver.TinyServer;

import java.util.HashMap;
import java.util.Map;

public class DependencyInjector {

  private final Map<Class, Object> instances = new HashMap<>();

  static {

  }

  public TinyServer tinyServer(){
    return new TinyServer(8282);
  }

  public ClassInstancesFinderResource classInstancesFinderResource(TinyServer tinyServer){
    return new ClassInstancesFinderResource(tinyServer);
  }

  public ClassInstancesFilterResource classInstancesFilterResource(
    TinyServer tinyServer,
    ObjectMapper objectMapper,
    ClassInstanceService classInstanceService
  ){
    return new ClassInstancesFilterResource(tinyServer, classInstanceService, objectMapper);
  }

  public<T> T get(Class<T> k) {
    return (T) instances.get(k);
  }
}
