package com.mageddo.kafka;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.vertx.core.eventbus.EventBus;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

/**
 * A simple resource retrieving the "in-memory" "my-data-stream" and sending the items to a server sent event.
 */
@Path("/prices")
public class PriceResource {

  @Inject
  @Channel("my-data-stream")
  Publisher<Double> prices;

  @Inject
  @Channel("price-create")
  Emitter<Integer> priceEmitter;

  @GET
  @Path("/stream")
  @Produces(MediaType.SERVER_SENT_EVENTS) // denotes that server side events (SSE) will be produced
  @SseElementType("text/plain") // denotes that the contained data, within this SSE, is just regular text/plain data
  public Publisher<Double> stream() {
    return prices;
  }

  @POST
  @Consumes(MediaType.TEXT_PLAIN)
  public void addPrice(Integer price) {
    priceEmitter.send(price);
  }
}
