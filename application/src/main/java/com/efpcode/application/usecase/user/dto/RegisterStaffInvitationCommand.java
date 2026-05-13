package com.efpcode.application.usecase.user.dto;

import java.time.Instant;

public record RegisterStaffInvitationCommand(String inviteeEmail, String role, Instant expiresAt) {

  @Override
  public String toString() {
    return "RegisterStaffInvitationCommand{"
        + "inviteeEmail='<redacted>'"
        + '\''
        + ", role='"
        + role
        + '\''
        + ", expiresAt="
        + expiresAt
        + '}';
  }
}
