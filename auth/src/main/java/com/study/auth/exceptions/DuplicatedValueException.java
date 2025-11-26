package com.study.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatedValueException extends RuntimeException {

  private static final long serialVersionUID = -774503614558647443L;

  public DuplicatedValueException(String message) {
    super(message);
  }
}
