package com.efpcode.application.usecase.user.exceptions;

public class PasswordFailException extends UserApplicationException {
  public PasswordFailException(String message) {
    super(message);
  }
}
