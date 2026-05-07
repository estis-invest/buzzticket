package com.efpcode.infrastructure.web.dto;

import com.efpcode.application.usecase.auth.dto.AuthenticatedUserResult;
import java.util.UUID;

public record AuthenticatedUserResponse(
    UUID id, String name, String email, String role, String status, UUID partnerId) {
  public static AuthenticatedUserResponse from(AuthenticatedUserResult auth) {

    return new AuthenticatedUserResponse(
        auth.id(), auth.name(), auth.email(), auth.role(), auth.status(), auth.partnerId());
  }
}
