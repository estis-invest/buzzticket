package com.efpcode.application.usecase.auth.exceptions;

public class LoginFailException extends AuthApplicationException {
  public LoginFailException(String message) {
    super(message);
  }
}
