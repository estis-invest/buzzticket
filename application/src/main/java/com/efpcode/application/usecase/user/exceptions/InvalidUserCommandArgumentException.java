package com.efpcode.application.usecase.user.exceptions;

public class InvalidUserCommandArgumentException extends UserApplicationException {
  public InvalidUserCommandArgumentException(String message) {
    super(message);
  }
}
