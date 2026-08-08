package com.mageddo.fruit.resource;

import com.mageddo.fruit.dataformat.FruitReqV1;
import com.mageddo.fruit.dataformat.FruitResV1;
import com.mageddo.fruit.domain.Fruit;
import com.mageddo.fruit.dataprovider.mapper.FruitMapper;
import com.mageddo.fruit.service.FruitService;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Path("/fruits")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@RequiredArgsConstructor
public class FruitResource {

  private final FruitService service;

  @POST
  @Path("/create-if-absent")
  public FruitResV1 createIfAbsent(FruitReqV1 req) {
    final Fruit fruit = FruitMapper.of(req);
    final Fruit persisted = this.service.createIfAbsent(fruit);
    return FruitMapper.to(persisted);
  }

  @PUT
  @Path("/upsert")
  public FruitResV1 upsert(FruitReqV1 req) {
    final Fruit fruit = FruitMapper.of(req);
    final Fruit persisted = this.service.save(fruit);
    return FruitMapper.to(persisted);
  }

  @GET
  @Path("/{id}")
  public Response find(@PathParam("id") UUID id) {
    final Fruit fruit = this.service.find(id);

    if (fruit == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    return Response.ok(FruitMapper.to(fruit)).build();
  }
}
