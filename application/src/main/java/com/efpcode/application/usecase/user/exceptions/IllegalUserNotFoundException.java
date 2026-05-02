package com.efpcode.application.usecase.user.exceptions;

public class IllegalUserNotFoundException extends UserApplicationException {
  public IllegalUserNotFoundException(String message) {
    super(message);
  }
}
