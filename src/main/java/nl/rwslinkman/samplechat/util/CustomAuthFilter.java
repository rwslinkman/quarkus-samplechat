 package nl.rwslinkman.samplechat.util;

 import nl.rwslinkman.samplechat.data.UserProfile;

 import javax.ws.rs.container.ContainerRequestContext;
 import javax.ws.rs.container.ContainerRequestFilter;
 import javax.ws.rs.container.PreMatching;
 import javax.ws.rs.core.Response;
 import javax.ws.rs.core.SecurityContext;
 import javax.ws.rs.ext.Provider;
 import java.io.IOException;
 import java.security.Principal;
 import java.util.Optional;

@Provider
@PreMatching
public class CustomAuthFilter implements ContainerRequestFilter {

    public static final String AUTH_TOKEN_HEADER_KEY = "X-SampleChat-Auth";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if(requestContext.getUriInfo().getPath().startsWith("/api")) {

            String chatToken = requestContext.getHeaders().getFirst(AUTH_TOKEN_HEADER_KEY);
            if(StringUtils.isEmpty(chatToken)) {
                // No token passed in headers
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
                return;
            }

            Optional<UserProfile> authUser = UserProfile.find("chatToken", chatToken).firstResultOptional();
            if(authUser.isEmpty()) {
                // No user found with given token
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
                return;
            }

            // User found & authenticated
            requestContext.setSecurityContext(new ApiUserSecurityContext(authUser.get()));
        }
    }

    private static class ApiUserSecurityContext implements SecurityContext {

        private final UserProfile authUser;

        public ApiUserSecurityContext(UserProfile apiUser) {
            this.authUser = apiUser;
        }

        @Override
        public Principal getUserPrincipal() {
            return authUser;
        }

        @Override
        public boolean isUserInRole(String s) {
            return authUser.role.equals(s);
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public String getAuthenticationScheme() {
            return "basic";
        }
    }
}
