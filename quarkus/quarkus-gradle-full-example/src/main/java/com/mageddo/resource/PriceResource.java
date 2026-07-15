package com.mageddo.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.mageddo.domain.Stock;
import com.mageddo.service.StockPriceService;

import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.quarkus.scheduler.Scheduled;
import lombok.RequiredArgsConstructor;

/**
 * A simple resource retrieving the "in-memory" "my-data-stream" and sending the items to a server sent event.
 */
@Path("/prices")
@RequiredArgsConstructor
public class PriceResource {

  private final StockPriceService stockPriceService;
  private final StockPricePublisher publisher = new StockPricePublisher();

  @GET
  @Path("/stream")
  @Produces(MediaType.SERVER_SENT_EVENTS) // denotes that server side events (SSE) will be produced
  @SseElementType("text/plain") // denotes that the contained data, within this SSE, is just regular text/plain data
  public Publisher<String> stream() {
    return this.publisher;
  }

  @POST
  @Consumes(MediaType.TEXT_PLAIN)
  public void addPrice(BigDecimal price) {
    this.stockPriceService.updateStockPrice(Stock
        .builder()
        .symbol("PAGS")
        .price(price)
        .build()
    );
  }

  @Scheduled(every = "PT3S")
  public void refresh(){
    final var stocks = this.stockPriceService.find();
    if (stocks.isEmpty()) {
      return;
    }
    this.publisher.notify(stocks.get(0).getPrice());
  }

  private class StockPricePublisher implements Publisher<String> {

    private List<Subscriber<?super String>> subscribers = new ArrayList<>();

    @Override
    public void subscribe(Subscriber<? super String> s) {
      this.subscribers.add(s);
    }

    public void notify(BigDecimal price){
      this.subscribers.forEach(it -> it.onNext(String.valueOf(price)));
    }
  }
}
