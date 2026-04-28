package com.efpcode.infrastructure.web.dto;

import com.efpcode.application.usecase.bootstrap.dto.BootstrapResult;
import java.util.UUID;

public record BootstrapResponse(
    UUID partnerId,
    String partnerName,
    UUID adminUserId,
    String adminUserName,
    String adminUserEmail) {

  public static BootstrapResponse from(BootstrapResult result) {
    return new BootstrapResponse(
        result.partner().id().partnerId(),
        result.partner().name().partnerName(),
        result.user().id().id(),
        result.user().name().name(),
        result.user().email().email());
  }
}
