package com.efpcode.application.usecase.user.dto;

public record RegisterCustomerCommand(String name, String email, String password) {
  @Override
  public String toString() {
    return "RegisterCustomerCommand{"
        + "name='"
        + "REDACTED"
        + '\''
        + ", email='"
        + "REDACTED"
        + '\''
        + ", password='"
        + "REDACTED"
        + '\''
        + '}';
  }
}
