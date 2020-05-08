package com.mageddo.resource;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ExampleResourceTest {

  @Test
  public void testHelloEndpoint() {
    given()
        .when()
        .get("/hello")
        .then()
        .statusCode(200)
        .body(is("hello"));
  }

}
