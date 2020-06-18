package com.mageddo.jvmti.dependencies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mageddo.jvmti.agents.entrypoint.ClassInstancesFilterResource;
import com.mageddo.jvmti.agents.entrypoint.ClassInstancesFinderResource;
import com.mageddo.jvmti.classdelegate.ClassInstanceService;
import net.metzweb.tinyserver.TinyServer;

public class DependencyInjectionModule extends AbstractModule {

  @Provides
  static TinyServer tinyServer(){
    return new TinyServer(8282);
  }

  @Provides
  static ClassInstancesFinderResource classInstancesFinderResource(TinyServer tinyServer){
    return new ClassInstancesFinderResource(tinyServer, classInstanceService);
  }

  @Provides
  static ClassInstancesFilterResource classInstancesFilterResource(
    TinyServer tinyServer,
    ObjectMapper objectMapper,
    ClassInstanceService classInstanceService
  ){
    return new ClassInstancesFilterResource(tinyServer, classInstanceService, objectMapper);
  }

  @Provides
  static ObjectMapper objectMapper(){
    return new ObjectMapper();
  }
}
