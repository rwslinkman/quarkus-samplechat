package nl.rwslinkman.samplechat.data;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_profile")
@UserDefinition
public class UserProfile extends PanacheEntity {
    @Username
    public String username;
    @Password(PasswordType.CLEAR)
    public String password;
    @Roles
    public String role;

    /**
     * Adds a new user to the database
     * @param username the username
     * @param password the unencrypted password (it will be encrypted with bcrypt)
     * @param role the comma-separated roles
     */
    public static void add(String username, String password, String role) {
        UserProfile user = new UserProfile();
        user.username = username;
        user.password = password;
        user.role = role;
        user.persist();
    }
}