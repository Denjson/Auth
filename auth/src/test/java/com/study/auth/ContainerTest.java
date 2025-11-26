package com.study.auth;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.study.auth.details.DetailRepository;
import com.study.auth.details.DetailService;
import com.study.auth.user.UserRepository;
import com.study.auth.user.UserService;

import io.restassured.RestAssured;

/**
 * Testing containers: Postgres
 *
 * <p>To avoid warning for Mockito with Java 21+ add the Mockito agent direct path: IntelliJ IDEA:
 * Run/Debug Configurations → Edit Configurations → Templates → JUnit. In the VM options field, add
 * (for Windows)
 * -javaagent:C:\Users\Den\.m2\repository\org\mockito\mockito-core\5.17.0\mockito-core-5.17.0.jar
 * -Xshare:off
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ContainerTest {

  @LocalServerPort private Integer port;

  @Autowired UserService userService;
  @Autowired DetailService detailService;

  @Autowired UserRepository userRepository;
  @Autowired DetailRepository detailRepository;

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

  @BeforeAll
  static void beforeAll() {
    postgres.start();
  }

  @AfterAll
  static void afterAll() {
    postgres.stop();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    System.out.println("_____Postgres Host: " + postgres.getHost());
    System.out.println("_____Postgres Port Mapped: " + postgres.getMappedPort(5432));
    System.out.println("_____Postgres JdbcUrl: " + postgres.getJdbcUrl());
  }

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:" + port;
    //    userRepository.deleteAll();
  }

  @Test
  void
      givenPostgresContainerConfiguredWithDynamicProperties_whenCheckingRunningStatus_thenStatusIsRunning() {
    assertTrue(postgres.isRunning());
    System.out.println("_____Postgres is runnig");
    //    System.out.println("______All details: " + detailService.findAll());
  }

  //  @Test
  //  void shouldGetAllCustomers() {
  //    List<User> users =
  //        List.of(
  //            new User(null, "John", "Connor", LocalDateTime.now(), "john@mail.com"),
  //            new User(null, "Dennis", "Nix", LocalDateTime.now(), "dennis@mail.com"));
  //    userRepository.saveAll(users);
  //
  //    given()
  //        .contentType(ContentType.JSON)
  //        .when()
  //        .get("/api/v1/users")
  //        .then()
  //        .statusCode(200)
  //        .body(".", hasSize(2));
  //  }

  //  @Test
  //  void testDeleteDetail() {
  //    DetailRequest detailToDel = new DetailRequest(111, "John", "Smith");
  //    detailService.save(detailToDel);
  //    detailService.findAll();
  //
  //    //    detailRepository.save(detailToDel);
  //    // Testing that Detail is existing
  //    assertNotNull(detailService.findAll());
  //    System.out.println("______All details: " + detailService.findAll());
  //    // Deleting Detauk
  //    //    detailRepository.deleteById(detailToDel.getId());
  //    // Checking that User was deleted - method returns an Exception
  //    //    assertThrows(IdNotFoundException.class, () ->
  //    // detailRepository.findById(detailToDel.getId()));
  //  }
}
