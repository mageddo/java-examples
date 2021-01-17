package org.acme.security.jdbc;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import io.quarkus.security.identity.SecurityIdentity;

@Path("/my-account")
public class MyAccountResource {

  @Inject
  private SecurityIdentity securityIdentity;

  @Context
  private SecurityContext securityContext;

  @GET
  @PermitAll
  @Produces(MediaType.TEXT_PLAIN)
  public String myAccount() {
    return String.format(
        "name=%s%npassword=%s%nroles=%s%nanonymous=%b",
        this.securityIdentity.getPrincipal().getName(),
        this.securityIdentity.getCredentials(),
        this.securityIdentity.getRoles(),
        this.securityIdentity.isAnonymous()
    );
  }
}
