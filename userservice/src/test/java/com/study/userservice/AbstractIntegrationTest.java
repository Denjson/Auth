package com.study.userservice;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractIntegrationTest {

  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

  static final DockerImageName REDIS_IMAGE = DockerImageName.parse("redis:8.2.3");

  static final GenericContainer<?> redis =
      new GenericContainer<>(REDIS_IMAGE).withExposedPorts(6379);

  //   static KafkaContainer kafka = new KafkaContainer(
  //           DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));

  static {
    postgres.start();
    redis.start();
    //       kafka.start();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    System.out.println("_____Postgres Host: " + postgres.getHost());
    System.out.println("_____Postgres Port 5432 Mapped to: " + postgres.getMappedPort(5432));
    registry.add("spring.data.redis.host", () -> redis.getHost());
    registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    System.out.println("_____Redis Host: " + redis.getHost());
    System.out.println("_____Redis Port 6379 Mapped to: " + redis.getMappedPort(6379));
  }

  @BeforeAll
  static void beforeAll() {
    // register JDBC properties with your app using
    // postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()
    // register Kafka broker url with your app using kafka.getBootstrapServers()
  }

  @Test
  void givenRedisContainerConfiguredWithDynamicPropertiesIsRunning() {
    assertTrue(redis.isRunning());
    System.out.println("_____ Redis is runnig in container " + redis.getContainerName());
  }

  @Test
  void givenPostgresContainerConfiguredWithDynamicPropertiesIsRunning() {
    assertTrue(postgres.isRunning());
    System.out.println("_____ Postgre is runnig in container " + postgres.getContainerName());
  }
}
