package com.efpcode.application.usecase.auth.exceptions;

public class PasswordFailException extends AuthApplicationException {
  public PasswordFailException(String message) {
    super(message);
  }
}
