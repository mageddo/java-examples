package com.mageddo.resource;

import java.math.BigDecimal;
import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.mageddo.domain.Stock;

@Path("/stocks")
public class StockResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Stock> hello() {
    return List.of(Stock
        .builder()
        .symbol("PAGS")
        .price(BigDecimal.valueOf(Math.random() * 100))
        .build());
  }
}
