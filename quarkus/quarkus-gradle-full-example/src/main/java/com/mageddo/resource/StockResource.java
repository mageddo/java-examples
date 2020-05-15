package com.mageddo.resource;

import java.math.BigDecimal;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
