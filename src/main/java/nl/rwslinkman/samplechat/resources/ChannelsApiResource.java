package nl.rwslinkman.samplechat.resources;

import io.quarkus.logging.Log;
import nl.rwslinkman.samplechat.data.ChatChannel;
import nl.rwslinkman.samplechat.data.UserProfile;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Optional;

@Path("/api/channels")
public class ChannelsApiResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ChatChannel> listChannels() {
        return ChatChannel.listAll();
    }

    @GET
    @Path("/{name}")
    @RolesAllowed({"user", "admin", "superadmin"})
    @Produces(MediaType.APPLICATION_JSON)
    public ChatChannel getChannelData(@PathParam("name") String name, @Context SecurityContext securityContext) {
        UserProfile userPrincipal = (UserProfile) securityContext.getUserPrincipal();
        Log.info("Request made by " + userPrincipal.username);

        Optional<ChatChannel> foundChannel = ChatChannel.find("name", name).firstResultOptional();
        return foundChannel.orElse(null);
    }
}
