package com.efpcode.application.usecase.user.dto;

public record RegisterStaffCommand(String name, String email, String password, String role) {

  @Override
  public String toString() {
    return "RegisterStaffCommand{"
        + "name='"
        + name
        + '\''
        + ", email='"
        + "REDACTED"
        + '\''
        + ", password='"
        + "REDACTED"
        + '\''
        + ", role='"
        + role
        + '\''
        + '}';
  }
}
