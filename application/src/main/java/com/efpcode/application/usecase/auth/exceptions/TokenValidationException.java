package com.efpcode.application.usecase.auth.exceptions;

public class TokenValidationException extends AuthApplicationException {
  public TokenValidationException(String message) {
    super(message);
  }
}
