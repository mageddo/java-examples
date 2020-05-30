package com.mageddo.controller;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mageddo.thymeleaf.Thymeleaf;

import lombok.RequiredArgsConstructor;

@Path("/user")
@RequiredArgsConstructor
public class UserController {

  private final Thymeleaf thymeleaf;

  @GET
  @Path("/login")
  @Produces(MediaType.TEXT_HTML)
  public String index(){
    return this.thymeleaf.from("templates/user/login.html", Map.of(
        "loggedIn", true,
        "username", "Elvis"
    ));
  }
}
