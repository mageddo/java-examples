package com.mageddo.jvmti;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.mageddo.jvmti.classdelegate.LocalClassInstanceService;
import com.mageddo.jvmti.classdelegate.scanning.ReferenceFilterFactory;
import com.mageddo.jvmti.entrypoint.ClassInstanceFieldSetterResource;
import com.mageddo.jvmti.entrypoint.ClassInstancesFilterResource;
import com.mageddo.jvmti.entrypoint.ClassInstancesRetrieverResource;
import net.metzweb.tinyserver.TinyServer;

import javax.inject.Singleton;

public class DependencyInjectionModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ClassInstancesFilterResource.class).asEagerSingleton();
    this.bind(ClassInstancesRetrieverResource.class).asEagerSingleton();
    this.bind(ClassInstancesRetrieverResource.class).asEagerSingleton();
    this.bind(ClassInstanceFieldSetterResource.class).asEagerSingleton();
    this.bind(LocalClassInstanceService.class).in(Scopes.SINGLETON);
    this.bind(ReferenceFilterFactory.class).in(Scopes.SINGLETON);
  }

  @Provides
  @Singleton
  static TinyServer tinyServer(){
    System.out.println("created!!!!!!!!!!!!!!!!");
    return new TinyServer(8384);
  }

  @Provides
  @Singleton
  static ObjectMapper objectMapper(){
    return new ObjectMapper();
  }
}
