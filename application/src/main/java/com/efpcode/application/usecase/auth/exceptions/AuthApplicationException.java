package com.efpcode.application.usecase.auth.exceptions;

public abstract class AuthApplicationException extends RuntimeException {
  protected AuthApplicationException(String message) {
    super(message);
  }
}
