package com.efpcode.application.usecase.user.exceptions;

public class IllegalUserStatusException extends UserApplicationException {
  public IllegalUserStatusException(String message) {
    super(message);
  }
}
