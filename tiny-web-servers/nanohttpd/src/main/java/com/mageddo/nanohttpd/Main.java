package com.mageddo.nanohttpd;

import fi.iki.elonen.util.ServerRunner;

// https://www.baeldung.com/nanohttpd
public class Main {
  public static void main(String[] args) {
    ServerRunner.run(WebServer.class);
  }
}
