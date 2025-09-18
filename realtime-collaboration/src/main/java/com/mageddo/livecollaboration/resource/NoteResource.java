package com.mageddo.livecollaboration.resource;

import org.jboss.logging.Logger;

import com.mageddo.livecollaboration.mapper.NoteMapper;
import com.mageddo.livecollaboration.resource.vo.NoteReq;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/notes")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NoteResource {

  private static final Logger LOGGER = Logger.getLogger(NoteResource.class);

  private final NoteMapper noteMapper;

  @Inject
  public NoteResource(final NoteMapper noteMapper) {
    this.noteMapper = noteMapper;
  }

  @PUT
  @Path("/{noteId}")
  public Response updateNote(
      @PathParam("noteId") final String noteId,
      final NoteReq request
  ) {
    final var note = this.noteMapper.toDomain(noteId, request);
    LOGGER.infof("Note synchronized: %s", note);
    return Response.noContent().build();
  }
}
