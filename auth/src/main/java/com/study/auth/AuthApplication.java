package com.study.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.study.auth.service.AuthenticationService;
import com.study.auth.service.RegisterRequest;
import com.study.auth.user.Role;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuthApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuthApplication.class, args);
    System.out.println("Running! Auth Service.");
  }

  @Bean
  public CommandLineRunner commandLineRunner(AuthenticationService service) {
    return args -> {
      var admin =
          RegisterRequest.builder()
              .firstname("Admin")
              .lastname("Admin")
              .email("admin@mail.com")
              .password("admin")
              .role(Role.ADMIN)
              .build();
      System.out.println("Admin token: " + service.register(admin).getAccessToken());

      var manager =
          RegisterRequest.builder()
              .firstname("Manager")
              .lastname("Manager")
              .email("manager@mail.com")
              .password("manager")
              .role(Role.MANAGER)
              .build();
      System.out.println("Manager token: " + service.register(manager).getAccessToken());

      var user =
          RegisterRequest.builder()
              .firstname("User")
              .lastname("User")
              .email("user@mail.com")
              .password("user")
              .role(Role.USER)
              .build();
      System.out.println("User token: " + service.register(user).getAccessToken());
    };
  }
}
