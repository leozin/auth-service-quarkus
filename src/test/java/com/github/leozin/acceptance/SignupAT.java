package com.github.leozin.acceptance;

import com.github.leozin.dto.ErrorResponse;
import com.github.leozin.dto.SignupRequest;
import com.github.leozin.dto.SignupResponse;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.leozin.configuration.RestAssuredDefaults.givenJson;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class SignupAT extends BaseAcceptanceTest {

    @Test
    @DisplayName("Signup with all fields filled")
    public void testSignup() {
        SignupRequest request = new SignupRequest("foo@bar.com", "foobar", "foo", "bar");
        SignupResponse response = givenJson()
                .when()
                .body(request)
                .post("/signup")
                .then()
                .statusCode(200)
                .extract()
                .as(SignupResponse.class);

        assertThat(response.token(), notNullValue());
    }

    @Test
    @DisplayName("Signup with duplicated email")
    public void testSignupWithSameEmail() {
        SignupRequest request = new SignupRequest("iExist@email.com", "foobar", "foo", "bar");
        givenJson()
                .when()
                .body(request)
                .post("/signup")
                .then()
                .statusCode(200)
                .extract()
                .as(SignupResponse.class);

        givenJson()
                .when()
                .body(request)
                .post("/signup")
                .then()
                .statusCode(422);
    }

    @Test
    @DisplayName("Signup with invalid content")
    public void testSignupWithInvalidContent() {
        SignupRequest request = new SignupRequest(null, null, "foo", "bar");

        givenJson()
                .when()
                .body(request)
                .post("/signup")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Signup with empty content")
    public void testSignupWithEmptyContent() {
        givenJson()
                .when()
                .post("/signup")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Signup with invalid email")
    public void testSignupWithInvalidEmail() {
        SignupRequest request = new SignupRequest("wrong!", "foobar", "foo", "bar");
        ErrorResponse response = givenJson()
                .when()
                .body(request)
                .post("/signup")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);

        assertEquals("signup.request.email (wrong!): must be a well-formed email address",
                response.message());
    }
}