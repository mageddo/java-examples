package com.mageddo.controller;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.mageddo.thymeleaf.Thymeleaf;

import lombok.RequiredArgsConstructor;

@Path("/")
@RequiredArgsConstructor
public class StockController {

  private final Thymeleaf thymeleaf;

  @GET
  @Path("/stocks")
  public String stocks(){
    return this.thymeleaf.from("templates/stock/stocks.html", Map.of());
  }
}
