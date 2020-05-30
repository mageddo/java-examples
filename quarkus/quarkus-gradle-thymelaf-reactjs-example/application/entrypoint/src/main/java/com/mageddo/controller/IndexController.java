package com.mageddo.controller;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.mageddo.thymeleaf.Thymeleaf;

import lombok.RequiredArgsConstructor;

@Path("/")
@RequiredArgsConstructor
public class IndexController {

  private final Thymeleaf thymeleaf;

  @GET
  public String index(){
    return this.thymeleaf.from("templates/index.html", Map.of());
  }
}
