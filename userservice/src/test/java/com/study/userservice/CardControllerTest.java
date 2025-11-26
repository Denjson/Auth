package com.study.userservice;

import static io.restassured.RestAssured.given;
// import static org.junit.Assert.assertNotNull;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.study.userservice.dto.UserResponseDTO;
import com.study.userservice.entity.User;
import com.study.userservice.exceptions.IdNotFoundException;
import com.study.userservice.repository.CardRepository;
import com.study.userservice.repository.UserRepository;
import com.study.userservice.service.interfaces.CardService;
import com.study.userservice.service.interfaces.UserService;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

// import redis.clients.jedis.Jedis;

/**
 * Testing: CardController with Postgres and Redis containers
 *
 * <p>To avoid warning for Mockito with Java 21+ add the Mockito agent direct path: IntelliJ IDEA:
 * Run/Debug Configurations → Edit Configurations → Templates → JUnit. In the VM options field, add
 * (for Windows)
 * -javaagent:C:\Users\Den\.m2\repository\org\mockito\mockito-core\5.17.0\mockito-core-5.17.0.jar
 * -Xshare:off
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @Testcontainers
class CardControllerTest extends AbstractIntegrationTest {

  static long userId = 0;

  @LocalServerPort private Integer port;

  UserService userService;
  UserRepository userRepository;

  CardService cardService;
  CardRepository cardRepository;

  @Autowired
  public CardControllerTest(
      UserService userService,
      UserRepository userRepository,
      CardService cardService,
      CardRepository cardRepository) {
    this.userService = userService;
    this.userRepository = userRepository;
    this.cardService = cardService;
    this.cardRepository = cardRepository;
  }

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:" + port;
    cardRepository.deleteAll();
    if (userId == 0) {
      userId = userService.addTestUser().getFirst().getId();
    }
  }

  @Test
  void givenRedisContainerConfiguredWithDynamicPropertiesIsRunning() {
    printCurrentMethodName();
    assertTrue(redis.isRunning());
    System.out.println("_____Redis is runnig in container");
  }

  @Test
  void createCardTest() {
    String payload =
"""
{"user_id":currentUserId,"number":"123456789","holder":"CardHolder","expiration_date":"2022-02-16T10:22:15","active":"true"}
        """;

    payload = payload.replace("currentUserId", "" + userId);

    Response responseController =
        given()
            .contentType(ContentType.JSON)
            .body(payload)
            .when()
            .post("/api/v1/card")
            .then()
            .statusCode(200)
            .body("holder", equalTo("CardHolder"))
            .extract()
            .response();
    assertNotNull(responseController.asString());
    printCurrentMethodName(responseController);
  }

  @Test
  void createCardsTest() {
    String payload =
        """
  [{"user_id":currentUserId,"number":"111111","holder":"CardHolder","expiration_date":"2022-02-16T10:22:15","active":"true"},
  {"user_id":currentUserId,"number":"222222","holder":"CardHolder","expiration_date":"2022-02-16T10:22:15","active":"true"}]
          """;
    payload = payload.replace("currentUserId", "" + userId);

    Response responseController =
        given()
            .contentType(ContentType.JSON)
            .body(payload)
            .when()
            .post("/api/v1/cards")
            .then()
            .statusCode(200)
            .body(".", hasSize(2))
            .extract()
            .response();
    assertNotNull(responseController.asString());
    printCurrentMethodName(responseController);
  }

  @Test
  void getCardByIdTest() {
    UserResponseDTO userResponseDTO = userService.getRandomUser();
    long cardId = cardService.addRandomCard(userResponseDTO).getId();

    Response responseController =
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/card/" + cardId)
            .then()
            .statusCode(200)
            .body("holder", equalTo(userResponseDTO.getName()))
            .extract()
            .response();
    assertNotNull(responseController.asString());
    printCurrentMethodName(responseController);
  }

  @Test
  void getCardsByIdsTest() {
    UserResponseDTO userResponseDTO = userService.getRandomUser();
    Long id1 = cardService.addRandomCard(userResponseDTO).getId();
    Long id2 = cardService.addRandomCard(userResponseDTO).getId();

    Response responseController =
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/cards/" + id1 + "," + id2)
            .then()
            .statusCode(200)
            .body(".", hasSize(2))
            .extract()
            .response();
    assertNotNull(responseController.asString());
    printCurrentMethodName(responseController);
  }

  @Test
  void updateCardTest() {
    UserResponseDTO userResponseDTO = userService.getRandomUser();
    Long id = cardService.addRandomCard(userResponseDTO).getId();

    String payload =
        """
  {"user_id":currentUserId,"number":"987654","holder":"CannotChangeCardHolder","expiration_date":"2022-02-16T10:22:15","active":"true"}
          """;
    payload = payload.replace("currentUserId", "" + userId);

    Response responseController =
        given()
            .contentType(ContentType.JSON)
            .body(payload)
            .when()
            .put("/api/v1/card/" + id)
            .then()
            .statusCode(200)
            .body("holder", equalTo(userResponseDTO.getName()))
            .extract()
            .response();
    assertNotNull(responseController.asString());
    //    assertTrue(responseController.asString().equals("[]"));
    printCurrentMethodName(responseController);
  }

  @Test
  void addCardTest() {
    UserResponseDTO userResponseDTO = userService.getRandomUser();
    Response responseController =
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/card/random")
            .then()
            .statusCode(200)
            .body("holder", equalTo(userResponseDTO.getName()))
            .extract()
            .response();
    assertNotNull(responseController.asString());
    printCurrentMethodName(responseController);
  }

  @Test
  void getAllCardsTest() {
    UserResponseDTO userResponseDTO = userService.getRandomUser();
    cardService.addRandomCard(userResponseDTO);
    cardService.addRandomCard(userResponseDTO);
    assertTrue(cardService.getAllCards().size() == 2);
    Response responseController =
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/cards")
            .then()
            .statusCode(200)
            .body(".", hasSize(2))
            .extract()
            .response();
    printCurrentMethodName(responseController);
  }

  @Test
  void deleteCardById() {
    UserResponseDTO userResponseDTO = userService.getRandomUser();
    long id = cardService.addRandomCard(userResponseDTO).getId();

    Response responseController =
        given()
            .contentType(ContentType.JSON)
            .when()
            .delete("/api/v1/card/" + id)
            .then()
            .statusCode(200)
            .body("holder", equalTo(userResponseDTO.getName()))
            .extract()
            .response();
    assertNotNull(responseController.asString());
    printCurrentMethodName(responseController);
  }

  @Test
  void deleteLastCardTest() {
    UserResponseDTO userResponseDTO = userService.getRandomUser();
    cardService.addRandomCard(userResponseDTO).getId();

    Response responseController =
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("api/v1/card/last")
            .then()
            .statusCode(200)
            .body("holder", equalTo(userResponseDTO.getName()))
            .extract()
            .response();
    assertNotNull(responseController.asString());
    printCurrentMethodName(responseController);
  }

  @Test
  void testDeleteCustomer() {
    User userToDel =
        new User(null, "John", "Connor", LocalDateTime.now(), "tomjohn@mail.com", true);
    userRepository.save(userToDel);
    UserResponseDTO userResponseDTO = userService.getRandomUser();
    long id = cardService.addRandomCard(userResponseDTO).getId();
    assertNotNull(cardService.getById(id));
    cardService.deleteById(id);
    assertThrows(IdNotFoundException.class, () -> cardService.getById(userToDel.getId()));
    printCurrentMethodName();
  }

  public static void printCurrentMethodName() {
    Exception e = new Exception();
    StackTraceElement[] stackTraceElements = e.getStackTrace();
    String methodName = stackTraceElements[1].getMethodName();
    System.out.println("Method name: " + methodName + "()");
    System.out.println("_________________\n");
  }

  public static void printCurrentMethodName(Response responseController) {
    Exception e = new Exception();
    StackTraceElement[] stackTraceElements = e.getStackTrace();
    String methodName = stackTraceElements[1].getMethodName();
    System.out.println("Response of controller: " + responseController.asString());
    System.out.println("Method name: " + methodName + "()");
    System.out.println("_________________\n");
  }
}
