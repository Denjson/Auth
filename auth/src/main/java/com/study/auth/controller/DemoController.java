package com.study.auth.controller;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.auth.config.JwtService;

@RestController
@RequestMapping
public class DemoController {

  JwtService jwtService;

  public DemoController(JwtService jwtService) {
    super();
    this.jwtService = jwtService;
  }

  @GetMapping("/api/v1/demo-controller")
  public ResponseEntity<String> accessSecureResource(
      @RequestHeader("Authorization") String authorizationHeader) {
    String token = null;
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      token = authorizationHeader.substring(7);
    }

    if (token != null) {
      System.out.println("________");
      System.out.println("Received token: " + token);
      String note = "\nExtracted information from token:\n";
      String email = jwtService.extractUsername(token);
      String role = jwtService.extractRole(token);
      String id = jwtService.extractUserId(token);
      Date exp = jwtService.extractExpiration(token);
      return ResponseEntity.ok(
          "Access granted with token:\n"
              + token
              + "\n"
              + note
              + "\n Expired on: "
              + exp
              + "\n email: "
              + email
              + "\n Role: "
              + role
              + "\n UserId: "
              + id);

    } else {
      return ResponseEntity.status(401).body("Unauthorized: Token missing or invalid.");
    }
  }
}
