package com.efpcode.application.usecase.user.dto;

import java.time.Instant;
import java.util.UUID;

public record CreateStaffInvitationResult(
    UUID invitationId,
    String inviteeEmail,
    String role,
    String status,
    Instant expiresAt,
    String rawToken) {

  @Override
  public String toString() {
    return "CreateStaffInvitationResult{"
        + "invitationId="
        + invitationId
        + ", inviteeEmail='"
        + inviteeEmail
        + '\''
        + ", role='"
        + role
        + '\''
        + ", status='"
        + status
        + '\''
        + ", expiresAt="
        + expiresAt
        + ", rawToken='"
        + "'REDACTED'"
        + '\''
        + '}';
  }
}
