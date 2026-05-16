package com.efpcode.application.usecase.user.dto;

public record ChangeUserPasswordCommand(String currentPassword, String newPassword) {
  @Override
  public String toString() {
    return "ChangeUserPasswordCommand{"
        + "currentPassword='"
        + " REDACTED "
        + '\''
        + ", newPassword='"
        + " REDACTED "
        + '\''
        + '}';
  }
}
