package nl.rwslinkman.samplechat.service;

import io.quarkus.panache.common.Parameters;
import nl.rwslinkman.samplechat.data.UserProfile;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class UserProfileService {

    @Transactional
    public List<String> createUserWithResult(String providedUsername, String providedPassword) {
        List<String> errorList = new ArrayList<>();

        Map<String, Object> queryParams = Parameters.with("un", providedUsername).and("pw", providedPassword).map();
        Optional<UserProfile> existingUser = UserProfile
                .find("select u from UserProfile u where username = :un or password = :pw", queryParams)
                .firstResultOptional();
        if(existingUser.isPresent()) {
            // User already exists
            UserProfile existingUserProfile = existingUser.get();
            if(existingUserProfile.username.equals(providedUsername)) {
                String msg = String.format("Sorry, the username '%s' is already taken", providedUsername);
                errorList.add(msg);
            }
            if(existingUserProfile.password.equals(providedPassword)) {
                String msg = String.format("Sorry, the password '%s' is already taken by '%s'", providedPassword, existingUserProfile.username);
                errorList.add(msg);
            }
        }
        else {
            // Create and persist
            UserProfile.add(providedUsername, providedPassword, "user");
        }
        return errorList;
    }
}
