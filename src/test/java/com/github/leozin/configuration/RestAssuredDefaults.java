package com.github.leozin.configuration;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestAssuredDefaults {

    public static RequestSpecification givenJson() {
        return given().contentType(ContentType.JSON);
    }

}
