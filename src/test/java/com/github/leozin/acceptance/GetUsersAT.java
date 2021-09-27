package com.github.leozin.acceptance;

import com.github.leozin.dto.RetrieveUsersResponse;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.leozin.configuration.RestAssuredDefaults.givenJson;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class GetUsersAT extends BaseAcceptanceTest {

    @Test
    @DisplayName("Retrieve users with authentication")
    public void testGetUsersAuthenticated() {
        String getUsersEmail = "getUsers@email.com";
        String token = TestUtils.createUser(getUsersEmail, "foobar", "foo", "bar");

        RetrieveUsersResponse retrieveUsersResponse = givenJson()
                .header("x-authentication-token", token)
                .when().get("/users")
                .then()
                .statusCode(200)
                .extract().as(RetrieveUsersResponse.class);

        var expectedResponse = new RetrieveUsersResponse.User(getUsersEmail, "foo", "bar");

        assertThat(retrieveUsersResponse.users().length, equalTo(1));
        assertThat(retrieveUsersResponse.users()[0], equalTo(expectedResponse));
    }

    @Test
    @DisplayName("Retrieve users without authentication")
    public void testGetUsersWithoutAuthentication() {
        given()
                .when().get("/users")
                .then()
                .statusCode(401);
    }
}