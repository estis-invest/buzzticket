package com.efpcode.application.usecase.auth.exceptions;

public class InvalidAuthenticationException extends AuthApplicationException {
  public InvalidAuthenticationException(String message) {
    super(message);
  }
}
