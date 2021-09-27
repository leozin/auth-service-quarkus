package com.github.leozin.acceptance;

import com.github.leozin.dto.RetrieveUsersResponse;
import com.github.leozin.dto.UpdateUserDetailsRequest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.leozin.configuration.RestAssuredDefaults.givenJson;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class PutUserDetailsAT extends BaseAcceptanceTest {

    @Test
    @DisplayName("Change user details")
    public void changeUserDetails() {
        // given
        String token = TestUtils.createUser("foo@bar.com", "foobar", "foo", "bar");

        // when
        UpdateUserDetailsRequest updateUserDetailsRequest = new UpdateUserDetailsRequest("newfoo", "newbar");
        givenJson()
                .when()
                .header("x-authentication-token", token)
                .body(updateUserDetailsRequest)
                .put("/users")
                .then()
                .statusCode(204);

        // then
        RetrieveUsersResponse retrieveUsersResponse = givenJson()
                .header("x-authentication-token", token)
                .when().get("/users")
                .then()
                .statusCode(200)
                .extract().as(RetrieveUsersResponse.class);

        assertEquals(retrieveUsersResponse.users()[0].firstName(), "newfoo");
        assertEquals(retrieveUsersResponse.users()[0].lastName(), "newbar");
    }

    @Test
    @DisplayName("Change user details without authentication")
    public void changeUserDetailsWithoutAuthentication() {
        // when
        UpdateUserDetailsRequest updateUserDetailsRequest = new UpdateUserDetailsRequest("a", "b");
        givenJson()
                .when()
                .body(updateUserDetailsRequest)
                .put("/users")
                .then()
                .statusCode(401);
    }
}