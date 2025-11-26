package com.study.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.study.auth.config.JwtService;
import com.study.auth.service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class LogoutController {

  private final LogoutHandler logoutHandler;

  JwtService jwtService;
  AuthenticationService authenticationService;

  public LogoutController(
      LogoutHandler logoutHandler,
      JwtService jwtService,
      AuthenticationService authenticationService) {
    super();
    this.logoutHandler = logoutHandler;
    this.jwtService = jwtService;
    this.authenticationService = authenticationService;
  }

  @GetMapping("/api/v1/auth/logout")
  public ResponseEntity<String> performLogout(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
    String token = null;
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      token = authorizationHeader.substring(7);
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {

      logoutHandler.logout(request, response, authentication);

      String note = "\nToken is not active anymore:\n";
      Boolean expired = authenticationService.isTokenExpired(token);

      String email = jwtService.extractUsername(token);
      Boolean revoked = authenticationService.isTokenExpired(token);

      SecurityContextHolder.clearContext();

      return ResponseEntity.ok(
          note
              + token
              + "\nExpired: "
              + expired
              + "\nRevoked: "
              + revoked
              + "\nE-mail: "
              + email
              + "\nLogged out successfully");
    }

    return ResponseEntity.ok("Logout");
  }
}
