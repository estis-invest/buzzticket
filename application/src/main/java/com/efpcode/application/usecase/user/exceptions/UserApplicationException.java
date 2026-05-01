package com.efpcode.application.usecase.user.exceptions;

public abstract class UserApplicationException extends RuntimeException {
  protected UserApplicationException(String message) {
    super(message);
  }
}
