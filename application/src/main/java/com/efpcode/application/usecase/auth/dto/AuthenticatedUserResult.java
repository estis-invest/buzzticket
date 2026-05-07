package com.efpcode.application.usecase.auth.dto;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.user.model.User;
import java.util.UUID;

public record AuthenticatedUserResult(
    UUID id, String name, String email, String role, String status, UUID partnerId) {

  public static AuthenticatedUserResult from(User user) {
    return new AuthenticatedUserResult(
        user.id().id(),
        user.name().name(),
        user.email().email(),
        user.role().name(),
        user.status().name(),
        user.partnerId().map(PartnerId::partnerId).orElse(null));
  }
}
