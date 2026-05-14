package com.efpcode.application.usecase.user.dto;

import java.time.Instant;
import java.util.UUID;

public record StaffInvitationQueryResult(
    UUID invitationId,
    String inviteeEmail,
    String role,
    String status,
    Instant expiresAt,
    Instant updatedAt) {
  @Override
  public String toString() {
    return "StaffInvitationQueryResult{"
        + "invitationId="
        + invitationId
        + ", inviteeEmail='"
        + "REDACTED"
        + '\''
        + ", role='"
        + role
        + '\''
        + ", status='"
        + status
        + '\''
        + ", expiresAt="
        + expiresAt
        + ", updatedAt="
        + updatedAt
        + '}';
  }
}
