package com.github.leozin.sanity;


import com.github.leozin.dto.*;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static com.github.leozin.configuration.RestAssuredDefaults.givenJson;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthServiceSanityCheck {

    public static final String AUTH_SERVICE_HOST_ENV_VAR = "AUTH_SERVICE_HOST";
    private static String validToken = null;

    @BeforeAll
    public static void setup() {
        // checks if var is in env vars or execution properties
        String hostname = System.getenv(AUTH_SERVICE_HOST_ENV_VAR);
        if (hostname == null) {
            hostname = System.getProperty(AUTH_SERVICE_HOST_ENV_VAR);
        }
        System.out.println(AUTH_SERVICE_HOST_ENV_VAR + " is " + hostname);
        if (hostname == null) {
            System.out.println(AUTH_SERVICE_HOST_ENV_VAR + " is not set. Setting to localhost");
            hostname = "localhost:8080";
        }
        RestAssured.baseURI = "http://" + hostname;
    }

    @Test
    @DisplayName("Test health check endppints")
    @Order(1)
    public void healthCheck() {
        String liveURL = "/q/health/live";
        String readyURL = "/q/health/ready";
        String health = "/q/health";

        givenJson()
                .when().get(liveURL)
                .then()
                .assertThat()
                .statusCode(200)
                .body("status", equalTo("UP"));

        givenJson()
                .when().get(readyURL)
                .then()
                .assertThat()
                .statusCode(200)
                .body("status", equalTo("UP"))
                .body("checks[0].name", equalTo("Database connections health check"))
                .body("checks[0].status", equalTo("UP"));

        givenJson()
                .when().get(health)
                .then()
                .assertThat()
                .statusCode(200)
                .body("checks[0].name", equalTo("Database connections health check"))
                .body("checks[0].status", equalTo("UP"));
    }

    @Test
    @DisplayName("Test signup and login with generated user")
    @Order(2)
    public void sanityTestSignup() {
        String uuid = UUID.randomUUID() + "@test.com";

        SignupRequest request = new SignupRequest(uuid, "foobar", "foo", "bar");
        SignupResponse signupResponse = givenJson()
                .when()
                .body(request)
                .post("/signup")
                .then()
                .statusCode(200)
                .extract()
                .as(SignupResponse.class);

        assertThat(signupResponse.token(), notNullValue());

        LoginRequest loginRequest = new LoginRequest(uuid, "foobar");
        LoginResponse loginResponse = givenJson()
                .when()
                .body(loginRequest)
                .post("/login")
                .then()
                .statusCode(200).extract().as(LoginResponse.class);

        assertNotEquals(signupResponse.token(), loginResponse.token());

        validToken = loginResponse.token();
    }

    @Test
    @DisplayName("Test signup and login with generated user")
    @Order(3)
    public void sanityCheckGetUsers() {
        RetrieveUsersResponse retrieveUsersResponse = givenJson()
                .header("x-authentication-token", validToken)
                .when().get("/users")
                .then()
                .statusCode(200)
                .extract().as(RetrieveUsersResponse.class);

        assertTrue(retrieveUsersResponse.users().length > 0);
    }
}
