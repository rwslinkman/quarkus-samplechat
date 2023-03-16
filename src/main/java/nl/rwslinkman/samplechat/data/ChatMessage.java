package nl.rwslinkman.samplechat.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "chat_message")
public class ChatMessage extends PanacheEntity {

    public String message;

    @ManyToOne
    public UserProfile sender;

    @ManyToOne
    @JsonBackReference
    public ChatChannel recipient;

    public ZonedDateTime sentAt;

    public static ChatMessage postNew(String message, UserProfile sender, ChatChannel recipient) {
        ChatMessage cm = new ChatMessage();
        cm.message = message;
        cm.sender = sender;
        cm.recipient = recipient;
        cm.sentAt = ZonedDateTime.now(ZoneId.systemDefault());
        cm.persist();
        return cm;
    }
}
