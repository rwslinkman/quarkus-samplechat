package nl.rwslinkman.samplechat.data;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_channel")
public class ChatChannel extends PanacheEntity {

    public String name;

    public String description;

    @ManyToMany
    @JsonManagedReference
    public List<UserProfile> members;

    @OneToMany
    @JsonManagedReference
    public List<ChatMessage> messages;

    public ChatChannel() {
        this.members = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public static ChatChannel create(String channelName, String channelDescription) {
        ChatChannel channel = new ChatChannel();
        channel.name = channelName;
        channel.description = channelDescription;
        channel.persist();
        return channel;
    }
}
