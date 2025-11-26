package com.study.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.study.auth.user.UserRepository;
import com.study.auth.user.UserService;

// import com.study.userservice.dto.UserResponseDTO;
// import com.study.userservice.entity.User;
// import com.study.userservice.exceptions.IdNotFoundException;
// import com.study.userservice.repository.UserRepository;
// import com.study.userservice.service.interfaces.UserService;

import io.restassured.RestAssured;

// import redis.clients.jedis.Jedis;

/**
 * Testing: UserController with Postgres and Redis containers
 *
 * <p>To avoid warning for Mockito with Java 21+ add the Mockito agent direct path: IntelliJ IDEA:
 * Run/Debug Configurations → Edit Configurations → Templates → JUnit. In the VM options field, add
 * (for Windows)
 * -javaagent:C:\Users\Den\.m2\repository\org\mockito\mockito-core\5.17.0\mockito-core-5.17.0.jar
 * -Xshare:off
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class TryTest {

  @LocalServerPort private Integer port;

  @Autowired UserService userService;
  @Autowired UserRepository userRepository;

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

  // private static final DockerImageName REDIS_IMAGE = DockerImageName.parse("redis:8.2.3");

  // @Container
  // private static final GenericContainer<?> redis =
  //   new GenericContainer<>(REDIS_IMAGE).withExposedPorts(6379);

  //  @BeforeAll
  //  static void beforeAll() {
  //    postgres.start();
  //    redis.start();
  //  }
  //
  //  @AfterAll
  //  static void afterAll() {
  //    postgres.stop();
  //    redis.stop();
  //  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    // registry.add("spring.data.redis.host", () -> redis.getHost());
    // System.out.println("_____Redis Host: " + redis.getHost());
    // registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    // System.out.println("_____Redis Port Mapped: " + redis.getMappedPort(6379));
  }

  @BeforeEach
  void setUp() {

    RestAssured.baseURI = "http://localhost:" + port;
    userRepository.findAll();
    System.out.println("____All users: " + userRepository.findAll());
  }

  @Test
  void givenRedisContainerConfiguredWithDynamicPropertiesIsRunning() {
    //   printCurrentMethodName();
    //   assertTrue(redis.isRunning());
    //   System.out.println("_____Redis is runnig in container");
  }
}
