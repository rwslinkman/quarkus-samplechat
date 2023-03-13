package nl.rwslinkman.samplechat.resources;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@QuarkusTest
class UserWebResourceTest {

    @Test
    void shouldAccessPublicWhenAnonymous() {
        get("/users/create")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void shouldNotAccessAdminWhenAnonymous() {
        get("/users/profile")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void shouldAccessAdminWhenAdminAuthenticated() {
        given()
                .auth().preemptive().basic("admin", "admin")
                .when()
                .get("/users/profile")
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    void shouldNotAccessUserWhenAdminAuthenticated() {
        given()
                .auth().preemptive().basic("user", "user")
                .when()
                .get("/users/profile")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }
}