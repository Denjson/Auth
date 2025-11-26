package com.study.auth.details;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DetailRequest {

  private Integer id;
  private String owner;
  private String detail;
}
