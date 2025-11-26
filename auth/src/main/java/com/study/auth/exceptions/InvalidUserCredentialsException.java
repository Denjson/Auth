package com.study.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidUserCredentialsException extends RuntimeException {

  private static final long serialVersionUID = 4514999256739198877L;

  public InvalidUserCredentialsException(String message) {
    super(message);
  }
}
