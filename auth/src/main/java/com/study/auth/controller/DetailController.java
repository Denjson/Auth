package com.study.auth.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.auth.details.Detail;
import com.study.auth.details.DetailRequest;
import com.study.auth.details.DetailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/details")
@RequiredArgsConstructor
public class DetailController {

  private final DetailService service;

  @PostMapping
  @PreAuthorize("hasAuthority('admin:create')")
  public ResponseEntity<?> save(@RequestBody DetailRequest request) {
    service.save(request);
    return ResponseEntity.accepted().build();
  }

  @GetMapping
  public ResponseEntity<List<Detail>> findAllBooks() {
    return ResponseEntity.ok(service.findAll());
  }
}
