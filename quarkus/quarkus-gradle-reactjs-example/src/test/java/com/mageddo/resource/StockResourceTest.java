package com.mageddo.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.quarkus.test.junit.QuarkusTest;
import testing.SingleInstancePostgresExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@ExtendWith(SingleInstancePostgresExtension.class)
@QuarkusTest
public class StockResourceTest {

  @Test
  public void mustGetStocks() {
    given()
        .when()
        .get("/stocks")
        .then()
        .statusCode(200)
        .log().all()
        .body("size()", is(1))
        .body("[0].symbol", equalTo("PAGS"));
  }

}
