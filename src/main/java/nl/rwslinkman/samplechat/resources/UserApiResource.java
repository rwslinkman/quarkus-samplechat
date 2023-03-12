package nl.rwslinkman.samplechat.resources;

import javax.ws.rs.Path;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

@Path("/api/users")
public class UserApiResource {

    @GET
    @RolesAllowed("user")
    @Path("/me")
    public String me(@Context SecurityContext securityContext) {
        return securityContext.getUserPrincipal().getName();
    }
}