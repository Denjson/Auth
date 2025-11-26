package com.study.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserAlreadyExistsException extends RuntimeException {

  private static final long serialVersionUID = -3638905766857411574L;

  public UserAlreadyExistsException(String message) {
    super(message);
  }
}
