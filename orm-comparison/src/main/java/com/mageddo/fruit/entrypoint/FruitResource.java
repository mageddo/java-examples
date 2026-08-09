package com.mageddo.fruit.entrypoint;

import com.mageddo.fruit.df.FruitReqV1;
import com.mageddo.fruit.df.FruitResV1;
import com.mageddo.fruit.FruitService;

import com.mageddo.fruit.mapper.FruitMapper;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Singleton
@Path("/fruits")
@RunOnVirtualThread
@RequiredArgsConstructor
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FruitResource {

  private final FruitService service;

  @POST
  @Path("/create-if-absent")
  public Response createIfAbsent(FruitReqV1 req) {
    final var fruit = FruitMapper.of(req);
    final var created = this.service.createIfAbsent(fruit);

    if (created) {
      return Response
          .status(Response.Status.CREATED)
          .build();
    }

    return Response
        .noContent()
        .build();
  }

  @PUT
  @Path("/upsert")
  public Response upsert(FruitReqV1 req) {
    final var fruit = FruitMapper.of(req);
    final var created = this.service.save(fruit);

    return Response
        .status(created ? Response.Status.CREATED : Response.Status.OK)
        .entity(FruitMapper.toDf(fruit))
        .build();
  }

  @GET
  public List<FruitResV1> findByName(@QueryParam("name") String name) {
    return this.service
        .findByName(name)
        .stream()
        .map(FruitMapper::toDf)
        .toList();
  }

  @GET
  @Path("/{id}")
  public Response find(@PathParam("id") UUID id) {
    final var fruit = this.service.find(id);

    if (fruit == null) {
      return Response
          .status(Response.Status.NOT_FOUND)
          .build();
    }

    return Response
        .ok(FruitMapper.toDf(fruit))
        .build();
  }
}
