package com.study.auth.details;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetailService {

  private final DetailRepository repository;

  public void save(DetailRequest request) {
    var detail =
        Detail.builder()
            .id(request.getId())
            .owner(request.getOwner())
            .detail(request.getDetail())
            .build();
    repository.save(detail);
  }

  public List<Detail> findAll() {
    return repository.findAll();
  }
}
