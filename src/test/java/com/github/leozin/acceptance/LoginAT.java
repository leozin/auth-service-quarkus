package com.github.leozin.acceptance;

import com.github.leozin.dto.ErrorResponse;
import com.github.leozin.dto.LoginRequest;
import com.github.leozin.dto.LoginResponse;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.leozin.configuration.RestAssuredDefaults.givenJson;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class LoginAT extends BaseAcceptanceTest {

    @Test
    @DisplayName("Login with created user, different tokens")
    public void testLoginWithCreatedUser() {
        String token = TestUtils.createUser("foo@bar.com", "foobar", "foo", "bar");

        LoginRequest loginRequest = new LoginRequest("foo@bar.com", "foobar");
        LoginResponse loginResponse = givenJson()
                .when()
                .body(loginRequest)
                .post("/login")
                .then()
                .statusCode(200).extract().as(LoginResponse.class);

        assertNotEquals(token, loginResponse.token());
    }

    @Test
    @DisplayName("Login with invalid password")
    public void testLoginWithWrongCredentials() {
        TestUtils.createUser("iam@correct.com", "correct", "foo", "bar");

        LoginRequest loginRequest = new LoginRequest("iam@correct.com", "incorrect");
        ErrorResponse response = givenJson()
                .when()
                .body(loginRequest)
                .post("/login")
                .then()
                .statusCode(401).extract().as(ErrorResponse.class);

        assertAll("Valid error message for wrong credentials",
                () -> assertEquals("Email and password do not match", response.message()),
                () -> assertEquals(401, response.httpCode())
        );
    }

    @Test
    @DisplayName("Login with a invalid request")
    public void testLoginWithInvalidRequest() {
        LoginRequest request = new LoginRequest(null, null);

        givenJson()
                .when()
                .body(request)
                .post("/login")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Login with empty content")
    public void testLoginWithEmptyContent() {
        givenJson()
                .when()
                .post("/login")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Login with invalid email")
    public void testLoginWithInvalidEmail() {
        LoginRequest request = new LoginRequest("wrong!", "foobar");
        ErrorResponse response = givenJson()
                .when()
                .body(request)
                .post("/login")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);

        assertEquals("login.request.email (wrong!): must be a well-formed email address",
                response.message());
    }
}