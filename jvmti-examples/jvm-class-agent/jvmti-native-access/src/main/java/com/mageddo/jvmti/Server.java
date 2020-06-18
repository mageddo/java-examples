package com.mageddo.jvmti;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.metzweb.tinyserver.TinyServer;

public class Server {

  private static Injector injector;

  public static void start(){
    if(injector != null){
      System.out.println("Server already started");
      return ;
    }
    System.out.println("Server starting...");
    injector = Guice.createInjector(new DependencyInjectionModule());
    injector.getInstance(TinyServer.class).start();
  }
}
