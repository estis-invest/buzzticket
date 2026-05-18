package com.efpcode.application.usecase.user.dto;

public record UserAccountDeactivationCommand(String currentPassword) {
  @Override
  public String toString() {
    return "UserAccountDeactivationCommand{" + "currentPassword='" + " REDACTED " + '\'' + '}';
  }
}
