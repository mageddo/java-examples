package org.acme.security.jdbc;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class HomeResource {
  @GET
   public String home(){
    return "home";
  }
}
