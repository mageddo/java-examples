package com.mageddo.nanohttpd;

import fi.iki.elonen.router.RouterNanoHTTPD;

public class WebServer extends RouterNanoHTTPD {
  public WebServer() {
    super(8080);
    this.addMappings();
  }

  @Override
  public void addMappings() {
    this.addRoute("/helloworld", HelloWorld.class);
  }
}
