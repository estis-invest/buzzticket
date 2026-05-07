package com.efpcode.application.usecase.auth.exceptions;

public class AuthenticatedUserNotFoundException extends AuthApplicationException {
  public AuthenticatedUserNotFoundException(String message) {
    super(message);
  }
}
