package com.mageddo.stock.entrypoint.controller;

import java.util.Map;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.mageddo.thymeleaf.Thymeleaf;

import lombok.RequiredArgsConstructor;

@Path("/user")
@RequiredArgsConstructor
public class UserController {

  private final Thymeleaf thymeleaf;

  @GET
  @Produces(MediaType.TEXT_HTML)
  public String index() {
    return this.thymeleaf.from(
        "templates/user/index.html",
        Map.of(
            "loggedIn", true,
            "username", "Elvis"
        )
    );
  }
}
