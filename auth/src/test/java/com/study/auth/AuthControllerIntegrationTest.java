package com.study.auth;

import static io.restassured.RestAssured.given;
// import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * Testing secondary Controllers and authorization
 *
 * <p>Before testing start Application on port 8090, and Postgres on port 5490
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerIntegrationTest extends AbstractIntegrationTest {

  @LocalServerPort private Integer port;
  private static String token;

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:" + port;
    System.out.println("_________________\n");
  }

  //  @BeforeAll
  @Test
  public void authenticationControllerTest() {
    printCurrentMethodName();

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
            .log()
            .body()
            .statusCode(200)
            .body("accessToken", notNullValue())
            .extract()
            .response();
    token = response.jsonPath().getString("accessToken");
    System.out.println("Registered Token: " + token);
    System.out.println("Status: " + response.getStatusLine());
  }

  @Test
  public void demoControllerTest() {
    printCurrentMethodName();
    Response responseController =
        given()
            //            .baseUri("http://localhost:8090/api/v1/demo-controller")
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/api/v1/demo-controller")
            .then()
            .log()
            .body()
            .statusCode(200)
            //            .body("name", equalTo("Access to secured endpoint"))
            .extract()
            .response();
    System.out.println("________Demo-controller response: " + responseController.asString());
    System.out.println(
        "________Demo-controller response: " + responseController.asString().substring(0, 25));
    assertEquals(responseController.asString().substring(0, 25), "Access granted with token");
    System.out.println("Demo-controller response: " + responseController.asString());
  }

  @Test
  public void detailControllerGetTest() {
    printCurrentMethodName();
    Response responseController =
        given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/api/v1/details")
            .then()
            .log()
            .body()
            .statusCode(200)
            .extract()
            .response();

    assertNotNull(responseController.asString());
    System.out.println("Detail-controller GET response: " + responseController.asString());
  }

  @Test
  public void detailControllerPostTest() {
    printCurrentMethodName();
    String loginPayload =
        """
	    		{"owner":"Jane", "detail": "Splash"}
	    		        """;

    Response responseController =
        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(loginPayload)
            .when()
            .post("/api/v1/details")
            .then()
            .log()
            .all()
            //            .body()
            .statusCode(202)
            .extract()
            .response();

    assertNotNull(responseController.asString());
    System.out.println("Detail-controller POST response: " + responseController.asString());
  }

  @Test
  public void adminControllerGetTest() {
    printCurrentMethodName();
    Response responseController =
        given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/api/v1/admin")
            .then()
            .log()
            .body()
            .statusCode(200)
            .extract()
            .response();

    assertEquals(responseController.asString(), "GET - admin controller");
    System.out.println("Admin controller GET response: " + responseController.asString());
  }

  @Test
  public void adminControllerPostTest() {
    printCurrentMethodName();
    Response responseController =
        given()
            .header("Authorization", "Bearer " + token)
            .when()
            .post("/api/v1/admin")
            .then()
            .log()
            .body()
            .statusCode(200)
            .extract()
            .response();

    assertEquals(responseController.asString(), "POST - admin controller");
    System.out.println("Admin controller POST response: " + responseController.asString());
  }

  @Test
  public void adminControllerPutTest() {
    printCurrentMethodName();
    Response responseController =
        given()
            .header("Authorization", "Bearer " + token)
            .when()
            .put("/api/v1/admin")
            .then()
            .log()
            .body()
            .statusCode(200)
            .extract()
            .response();

    assertEquals(responseController.asString(), "PUT - admin controller");
    System.out.println("Admin controller PUT response: " + responseController.asString());
  }

  @Test
  public void adminControllerDeleteTest() {
    printCurrentMethodName();
    Response responseController =
        given()
            .header("Authorization", "Bearer " + token)
            .when()
            .delete("/api/v1/admin")
            .then()
            .log()
            .body()
            .statusCode(200)
            .extract()
            .response();

    assertEquals(responseController.asString(), "DELETE - admin controller");
    System.out.println("Admin controller DELETE response: " + responseController.asString());
  }

  public static void printCurrentMethodName() {
    Exception e = new Exception();
    StackTraceElement[] stackTraceElements = e.getStackTrace();
    String methodName = stackTraceElements[1].getMethodName();
    System.out.println("The method name is: " + methodName + "()");
  }

  @AfterAll
  static void updatePasswordTest() {
    System.out.println("_________________\n");
    printCurrentMethodName();
    String loginPayload =
        """
	    		{"currentPassword":"1234","newPassword":"rdp", "confirmationPassword":"rdp"}
	    		        """;
    Response response =
        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(loginPayload)
            .when()
            .patch("/api/v1/users")
            .then()
            .log()
            .body()
            .statusCode(200)
            //            .body("access_token", notNullValue())
            .extract()
            .response();
    System.out.println("The password updated successfully!");
    System.out.println("Status: " + response.getStatusLine());

    loginPayload =
        """
		    		{"currentPassword":"rdp","newPassword":"1234", "confirmationPassword":"1234"}
		    		        """;
    response =
        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(loginPayload)
            .when()
            .patch("/api/v1/users")
            .then()
            .log()
            .body()
            .statusCode(200)
            .extract()
            .response();

    System.out.println("The password restored successfully!");
    System.out.println("Status: " + response.getStatusLine());
  }
}
