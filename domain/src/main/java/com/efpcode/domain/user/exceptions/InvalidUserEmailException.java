package com.efpcode.domain.user.exceptions;

public class InvalidUserEmailException extends UserDomainException {
  public InvalidUserEmailException(String message) {
    super(message);
  }
}
