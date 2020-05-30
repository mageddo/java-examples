package com.mageddo.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mageddo.usecase.domain.Stock;
import com.mageddo.usecase.service.StockPriceService;

import lombok.RequiredArgsConstructor;

@Path("/api/stocks")
@RequiredArgsConstructor
public class StockResource {

  private final StockPriceService stockPriceService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Stock> hello() {
    return this.stockPriceService.find();
  }
}
