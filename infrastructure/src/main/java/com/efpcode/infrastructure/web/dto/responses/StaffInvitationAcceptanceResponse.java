package com.efpcode.infrastructure.web.dto.responses;

import com.efpcode.application.usecase.user.dto.StaffInvitationAcceptanceResult;
import java.util.UUID;

public record StaffInvitationAcceptanceResponse(UUID uuid, String name, String role) {

  public static StaffInvitationAcceptanceResponse fromResult(
      StaffInvitationAcceptanceResult acceptanceResult) {
    return new StaffInvitationAcceptanceResponse(
        acceptanceResult.uuid(), acceptanceResult.name(), acceptanceResult.role());
  }
}
