package nl.rwslinkman.samplechat.resources;

import nl.rwslinkman.samplechat.data.ChatChannel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/api/channels")
public class ChannelsApiResource {

    @GET
//    @RolesAllowed({"user", "admin", "superadmin"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<ChatChannel> listChannels() {
        return ChatChannel.listAll();
    }

    @GET
    @Path("/{name}")
//    @RolesAllowed({"user", "admin", "superadmin"})
    @Produces(MediaType.APPLICATION_JSON)
    public ChatChannel getChannelData(@PathParam("name") String name) {
        Optional<ChatChannel> foundChannel = ChatChannel.find("name", name).firstResultOptional();
        return foundChannel.orElse(null);
    }
}
