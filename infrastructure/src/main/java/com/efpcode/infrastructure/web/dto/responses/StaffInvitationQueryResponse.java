package com.efpcode.infrastructure.web.dto.responses;

import com.efpcode.application.usecase.user.dto.StaffInvitationQueryResult;
import java.time.Instant;
import java.util.UUID;

public record StaffInvitationQueryResponse(
    UUID invitationId,
    String inviteeEmail,
    String role,
    String status,
    Instant expiresAt,
    Instant updatedAt) {

  public static StaffInvitationQueryResponse fromResult(StaffInvitationQueryResult result) {
    return new StaffInvitationQueryResponse(
        result.invitationId(),
        result.inviteeEmail(),
        result.role(),
        result.status(),
        result.expiresAt(),
        result.updatedAt());
  }
}
