package nl.rwslinkman.samplechat;

import io.quarkus.runtime.StartupEvent;
import nl.rwslinkman.samplechat.data.ChatChannel;
import nl.rwslinkman.samplechat.data.ChatMessage;
import nl.rwslinkman.samplechat.data.UserProfile;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;
import javax.transaction.Transactional;

@Singleton
public class SampleChatApp {

    @Transactional
    public void loadUsers(@Observes StartupEvent evt) {
        ChatMessage.deleteAll();
        ChatChannel.deleteAll();
        // sample channel
        ChatChannel main = ChatChannel.create("main", "The main channel of this SampleChat server");

        // reset and load all test users
        UserProfile.deleteAll();
        UserProfile rick = UserProfile.add("rick", "rick", "superadmin");
        rick.channelMemberships.add(main);
        main.members.add(rick);
        rick.persist();
        UserProfile admin = UserProfile.add("admin", "admin", "admin");
        admin.channelMemberships.add(main);
        main.members.add(admin);
        admin.persist();
        UserProfile user = UserProfile.add("user", "user", "user");
        user.channelMemberships.add(main);
        user.persist();
        main.members.add(user);

        // Sample message
        ChatMessage welcome = ChatMessage.postNew("Welcome to the very insecure SampleChat application. Hope you don't care too much about your privacy!", rick, main);
        main.messages.add(welcome);
        main.persist();
    }
}
