package com.github.leozin.acceptance;

import com.github.leozin.dto.SignupRequest;
import com.github.leozin.dto.SignupResponse;

import static com.github.leozin.configuration.RestAssuredDefaults.givenJson;

public class TestUtils {

    public static String createUser(String email, String password, String firstName, String lastName) {
        SignupRequest signupRequest = new SignupRequest(email, password, firstName, lastName);
        SignupResponse signupResponse = givenJson()
                .when()
                .body(signupRequest)
                .post("/signup")
                .then()
                .statusCode(200)
                .extract()
                .as(SignupResponse.class);
        return signupResponse.token();
    }

}
