package nl.rwslinkman.samplechat.resources;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import nl.rwslinkman.samplechat.data.UserProfile;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@Path("/users")
public class UserWebResource {

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance createUserPage();
        public static native TemplateInstance profilePage(UserProfile loggedInUser);
    }

    @GET
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    @Path("/create")
    public TemplateInstance showCreateUserForm() {
        return Templates.createUserPage();
    }

    @GET
    @RolesAllowed({"user", "admin", "superadmin"})
    @Produces(MediaType.TEXT_HTML)
    @Path("/profile")
    public TemplateInstance showProfilePage(@Context SecurityContext securityContext) {
        Principal userPrincipal = securityContext.getUserPrincipal();
        String username = userPrincipal.getName();

        UserProfile loggedInUser = UserProfile
                .find("username", username)
                .firstResult();
        return Templates.profilePage(loggedInUser);
    }

    @GET
    @PermitAll
    @Path("/logout")
    @Produces(MediaType.MEDIA_TYPE_WILDCARD)
    @Consumes(MediaType.WILDCARD)
    public Response forceLogout() {
        // Forcefully return HTTP 401 so result of XMLHttpRequest will clear browser's credential cache
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
