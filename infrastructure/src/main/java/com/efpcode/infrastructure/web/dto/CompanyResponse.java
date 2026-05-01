package com.efpcode.infrastructure.web.dto;

import com.efpcode.application.usecase.company.dto.CompanyResult;
import java.util.UUID;

public record CompanyResponse(
    UUID partnerId,
    String partnerName,
    UUID adminUserId,
    String adminUserName,
    String adminUserEmail) {

  public static CompanyResponse from(CompanyResult result) {
    return new CompanyResponse(
        result.partner().id().partnerId(),
        result.partner().name().partnerName(),
        result.user().id().id(),
        result.user().name().name(),
        result.user().email().email());
  }
}
