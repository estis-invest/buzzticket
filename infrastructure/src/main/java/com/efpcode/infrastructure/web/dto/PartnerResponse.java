package com.efpcode.infrastructure.web.dto;

import com.efpcode.domain.partner.model.Partner;
import java.time.Instant;
import java.util.UUID;

public record PartnerResponse(
    UUID id,
    String name,
    String city,
    String country,
    String isoCode,
    String status,
    Instant createdAt,
    Instant updatedAt) {

  public static PartnerResponse fromDomain(Partner partner) {
    return new PartnerResponse(
        partner.id().partnerId(),
        partner.name().partnerName(),
        partner.city().partnerCity(),
        partner.country().partnerCountry(),
        partner.isoCode().isoCode(),
        partner.status().name(),
        partner.createdAt().createdAt(),
        partner.updateAt().updatedAt());
  }
}
