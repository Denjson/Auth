package com.study.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IdNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -3641944237913518944L;

  public IdNotFoundException(String message) {
    super(message);
  }
}
