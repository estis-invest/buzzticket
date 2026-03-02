package com.efpcode.domain.user.exceptions;

public class InvalidUserPasswordException extends UserDomainException {
  public InvalidUserPasswordException(String message) {
    super(message);
  }
}
