package com.mageddo.nanohttpd;


import java.time.LocalDateTime;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.router.RouterNanoHTTPD.DefaultHandler;

public class HelloWorld extends DefaultHandler {

  @Override
  public String getText() {
    return "Hello World " + LocalDateTime.now();
  }

  @Override
  public String getMimeType() {
    return NanoHTTPD.MIME_PLAINTEXT;
  }

  @Override
  public Response.IStatus getStatus() {
    return Response.Status.OK;
  }
}
