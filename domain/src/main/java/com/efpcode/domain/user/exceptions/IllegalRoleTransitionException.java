package com.efpcode.domain.user.exceptions;

public class IllegalRoleTransitionException extends UserDomainException {
  public IllegalRoleTransitionException(String message) {
    super(message);
  }
}
