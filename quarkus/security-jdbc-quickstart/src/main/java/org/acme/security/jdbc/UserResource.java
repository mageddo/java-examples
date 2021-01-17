package org.acme.security.jdbc;

import io.quarkus.security.identity.SecurityIdentity;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/users")
public class UserResource {

  @Inject
  SecurityIdentity securityIdentity;

  @GET
  @RolesAllowed("user")
  @Path("/me")
  @Produces(MediaType.TEXT_PLAIN)
  public String me() {
    return securityIdentity.getPrincipal().getName();
  }
}
