package com.study.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmailAlreadyExistsException extends RuntimeException {

  private static final long serialVersionUID = -4416409571180400220L;

  public EmailAlreadyExistsException(String message) {
    super(message);
  }
}
