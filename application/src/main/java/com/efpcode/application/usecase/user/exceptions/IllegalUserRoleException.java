package com.efpcode.application.usecase.user.exceptions;

public class IllegalUserRoleException extends UserApplicationException {
  public IllegalUserRoleException(String message) {
    super(message);
  }
}
