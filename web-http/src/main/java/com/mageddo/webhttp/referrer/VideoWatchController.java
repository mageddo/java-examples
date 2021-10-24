package com.mageddo.webhttp.referrer;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.thymeleaf.Thymeleaf;

import org.jboss.resteasy.specimpl.MultivaluedTreeMap;

@Path("/referrers")
public class VideoWatchController {

  private final Thymeleaf thymeleaf;
  private final ObjectMapper objectMapper;

  @Inject
  public VideoWatchController(Thymeleaf thymeleaf, ObjectMapper objectMapper) {
    this.thymeleaf = thymeleaf;
    this.objectMapper = objectMapper;
  }

  @GET
  @Path("/watch")
  @Produces(MediaType.TEXT_HTML)
  public String watch(@Context HttpHeaders httpHeaders) throws Exception {
    final var headers = this.objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(new MultivaluedTreeMap<>(httpHeaders.getRequestHeaders()));
    return this.thymeleaf.from(
        "/templates/watch.html",
        Map.of("headers", headers)
    );
  }
  @GET
  @Path("/current")
  @Produces(MediaType.TEXT_PLAIN)
  public String getReferrer(@HeaderParam("Referer") String referrer){
    return referrer;
  }
}
