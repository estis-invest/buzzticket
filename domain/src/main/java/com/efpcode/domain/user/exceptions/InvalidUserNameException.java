package com.efpcode.domain.user.exceptions;

public class InvalidUserNameException extends UserDomainException {
  public InvalidUserNameException(String message) {
    super(message);
  }
}
