package com.study.userservice.auditing;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

class AuditorAwareImpl implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    // TODO Auto-generated method stub
    //    return Optional.empty();
    return Optional.of("Someone");
  }
}
