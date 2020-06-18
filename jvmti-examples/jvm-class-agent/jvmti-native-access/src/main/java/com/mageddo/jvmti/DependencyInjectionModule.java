package com.mageddo.jvmti;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mageddo.jvmti.agents.entrypoint.ClassInstancesFilterResource;
import com.mageddo.jvmti.agents.entrypoint.ClassInstancesFinderResource;
import com.mageddo.jvmti.classdelegate.ClassInstanceService;
import com.mageddo.jvmti.classdelegate.scanning.ReferenceFilterFactory;
import net.metzweb.tinyserver.TinyServer;

public class DependencyInjectionModule extends AbstractModule {

  @Provides
  static TinyServer tinyServer(){
    return new TinyServer(8282);
  }

  @Provides
  static ClassInstancesFinderResource classInstancesFinderResource(
    TinyServer tinyServer,
    ClassInstanceService classInstanceService
  ){
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
  static ReferenceFilterFactory referenceFilterFactory(){
    return new ReferenceFilterFactory();
  }

  @Provides
  static ClassInstanceService classInstanceService(ReferenceFilterFactory referenceFilterFactory){
    return new ClassInstanceService(referenceFilterFactory);
  }

  @Provides
  static ObjectMapper objectMapper(){
    return new ObjectMapper();
  }
}
