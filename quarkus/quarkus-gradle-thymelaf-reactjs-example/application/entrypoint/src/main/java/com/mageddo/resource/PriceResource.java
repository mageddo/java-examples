package com.mageddo.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mageddo.usecase.domain.Stock;
import com.mageddo.usecase.service.StockPriceService;

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
    this.publisher.notify(this.stockPriceService.find().get(0).getPrice());
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
