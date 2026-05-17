package com.efpcode.application.usecase.user.dto;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.user.model.User;
import java.time.Instant;
import java.util.UUID;

public record UserResult(
    UUID id,
    String name,
    String email,
    String role,
    String status,
    Instant createdAt,
    Instant updatedAt,
    UUID partnerId) {

  public static UserResult fromDomain(User user) {
    return new UserResult(
        user.id().id(),
        user.name().name(),
        user.email().email(),
        user.role().name(),
        user.status().name(),
        user.userCreatedAt().time(),
        user.userUpdateAt().updatedAt(),
        user.partnerId().map(PartnerId::partnerId).orElse(null));
  }
}
