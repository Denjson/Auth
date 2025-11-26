package com.study.auth;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/** Testing registration and authorization */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIntegrationTest extends AbstractIntegrationTest {

  @LocalServerPort private Integer port;

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:" + port;
  }

  @Test
  public void shouldReturnOKWithRegistration() {
    String loginPayload =
"""
{"firstname":"den","lastname":"rodny", "email":"den@rod.com", "password":"1234", "role":"ADMIN"}
        """;

    Response response =
        given()
            .contentType(ContentType.JSON)
            .body(loginPayload)
            .when()
            .post("/api/v1/auth/register")
            .then()
            .statusCode(200)
            //            .body("access_token", notNullValue())
            .body("accessToken", notNullValue())
            .extract()
            .response();
    System.out.println("Registered Token: " + response.jsonPath().getString("accessToken"));
    System.out.println("Status: " + response.getStatusLine());
  }

  @Test
  public void shouldReturnOKWithValidToken() {
    String loginPayload =
        """
          {
            "email": "den@rod.com",
            "password": "1234"
          }
        """;

    Response response =
        given()
            .contentType(ContentType.JSON)
            .body(loginPayload)
            .when()
            .post("/api/v1/auth/authenticate")
            .then()
            .statusCode(200)
            .body("accessToken", notNullValue())
            .extract()
            .response();
    System.out.println("Updated Token: " + response.jsonPath().getString("accessToken"));
    System.out.println("Status: " + response.getStatusLine());
  }

  @Test
  public void shouldReturnUnauthorizedOnInvalidLogin() {
    String loginPayload =
        """
          {
            "email": "invalid_user@test.com",
            "password": "wrongpassword"
          }
        """;

    given()
        .contentType("application/json")
        .body(loginPayload)
        .when()
        .post("/api/v1/auth/authenticate")
        .then()
        .statusCode(403);
  }
}
