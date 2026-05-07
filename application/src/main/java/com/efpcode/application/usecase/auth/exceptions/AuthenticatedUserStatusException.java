package com.efpcode.application.usecase.auth.exceptions;

public class AuthenticatedUserStatusException extends AuthApplicationException {
  public AuthenticatedUserStatusException(String message) {
    super(message);
  }
}
