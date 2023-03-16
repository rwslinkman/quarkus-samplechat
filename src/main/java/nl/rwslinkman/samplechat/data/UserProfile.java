package nl.rwslinkman.samplechat.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.*;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_profile")
@UserDefinition
public class UserProfile extends PanacheEntity implements Principal {
    @Username
    public String username;
    @Password(PasswordType.CLEAR)
    public String password;
    @Roles
    public String role;
    // Used in the chat API in a custom header
    public String chatToken;
    @ManyToMany(mappedBy = "members")
    @JsonBackReference
    public List<ChatChannel> channelMemberships;

    public UserProfile() {
        this.channelMemberships = new ArrayList<>();
    }

    /**
     * Adds a new user to the database
     * @param username the username
     * @param password the unencrypted password (it will be encrypted with bcrypt)
     * @param role the comma-separated roles
     */
    public static UserProfile add(String username, String password, String role) {
        UserProfile user = new UserProfile();
        user.username = username;
        user.password = password;
        user.role = role;
        user.chatToken = "n/a";
        user.persist();
        return user;
    }

    @Override
    public String getName() {
        return username;
    }
}