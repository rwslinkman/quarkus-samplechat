package nl.rwslinkman.samplechat.resources;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/users")
public class UserWebResource {

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance createUserPage();
        public static native TemplateInstance loginPage();
    }

    @GET
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    @Path("/create")
    public TemplateInstance create() {
        return Templates.createUserPage();
    }

    // TODO: Login page
    // TODO: Profile page
}
