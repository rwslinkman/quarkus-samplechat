package nl.rwslinkman.samplechat.resources;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import nl.rwslinkman.samplechat.data.UserProfile;
import nl.rwslinkman.samplechat.service.UserProfileService;
import nl.rwslinkman.samplechat.util.StringUtils;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Path("/users")
public class UserWebResource {

    @Context
    UriInfo uriInfo;

    @Inject
    UserProfileService userProfileService;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance createUserPage(
            String placeholderUsername,
            String placeholderPassword,
            List<String> formErrors
        );
        public static native TemplateInstance profilePage(UserProfile loggedInUser);
    }

    @GET
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    @Path("/create")
    public TemplateInstance showCreateUserForm() {
        return Templates.createUserPage("", "", Collections.emptyList());
    }

    @POST
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    @Path("/create")
    public Response handleCreateUser(@FormParam("register_username") String username,
                                             @FormParam("register_password") String password) {
        List<String> formErrors = new ArrayList<>();
        if(StringUtils.isEmpty(username)) {
            formErrors.add("Please provide a username");
        }
        if(StringUtils.isEmpty(password)) {
            formErrors.add("Please provide a password");
        }

        List<String> creationErrors = userProfileService.createUserWithResult(username, password);
        if(creationErrors.isEmpty()) {
            return Response.status(Response.Status.FOUND).location(uriInfo.getBaseUri()).build();
        } else {
            formErrors.addAll(creationErrors);
            return Response.ok().entity(UserWebResource.Templates.createUserPage(username, password, formErrors)).build();
        }
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
