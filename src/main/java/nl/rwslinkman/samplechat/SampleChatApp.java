package nl.rwslinkman.samplechat;

import io.quarkus.runtime.StartupEvent;
import nl.rwslinkman.samplechat.data.UserProfile;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;
import javax.transaction.Transactional;

@Singleton
public class SampleChatApp {

    @Transactional
    public void loadUsers(@Observes StartupEvent evt) {
        // reset and load all test users
        UserProfile.deleteAll();
        UserProfile.add("rick", "rick", "superadmin");
        UserProfile.add("admin", "admin", "admin");
        UserProfile.add("user", "user", "user");
    }
}
