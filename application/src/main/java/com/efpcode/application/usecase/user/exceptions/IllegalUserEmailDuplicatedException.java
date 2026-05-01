package com.efpcode.application.usecase.user.exceptions;

public class IllegalUserEmailDuplicatedException extends UserApplicationException {
  public IllegalUserEmailDuplicatedException(String message) {
    super(message);
  }
}
