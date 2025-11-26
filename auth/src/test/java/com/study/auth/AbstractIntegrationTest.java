package com.study.auth;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractIntegrationTest {

  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

  static {
    postgres.start();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    System.out.println("_____Postgres Host: " + postgres.getHost());
    System.out.println("_____Postgres Port 54 Mapped to: " + postgres.getMappedPort(5432));
    System.out.println("_____Postgres JdbcUrl: " + postgres.getJdbcUrl());
    System.out.println("_____Postgres DatabaseName: " + postgres.getDatabaseName());
    System.out.println("_____Postgres Password: " + postgres.getPassword());
    System.out.println("_____Postgres Username: " + postgres.getUsername());
  }

  @BeforeAll
  static void beforeAll() {
    // register JDBC properties with your app using
    // postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()
    // register Kafka broker url with your app using kafka.getBootstrapServers()
  }

  @Test
  void givenPostgresContainerConfiguredWithDynamicPropertiesIsRunning() {
    assertTrue(postgres.isRunning());
    System.out.println("_____ Postgre is runnig in container " + postgres.getContainerName());
  }
}
