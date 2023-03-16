package nl.rwslinkman.samplechat.resources;

import io.quarkus.logging.Log;
import nl.rwslinkman.samplechat.data.ChatChannel;
import nl.rwslinkman.samplechat.data.UserProfile;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    @POST
    @Path("/{name}")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response postChannelMessage(@Context SecurityContext securityContext, @PathParam("name") String channelName) {
        UserProfile userPrincipal = (UserProfile) securityContext.getUserPrincipal();
        Log.info("Request made by " + userPrincipal.username);
        // TODO accept JSON body and store in ChatMessage entity
        return null;
    }
}
