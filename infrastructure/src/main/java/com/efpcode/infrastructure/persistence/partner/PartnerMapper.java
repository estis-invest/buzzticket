package com.efpcode.infrastructure.persistence.partner;

import com.efpcode.domain.partner.model.*;

public final class PartnerMapper {
  private PartnerMapper() {}

  public static PartnerEntity toEntity(Partner domain) {
    PartnerEntity entity = new PartnerEntity();
    entity.setPartnerId(domain.id().partnerId());
    entity.setPartnerName(domain.name().partnerName());
    entity.setPartnerCity(domain.city().partnerCity());
    entity.setPartnerCountry(domain.country().partnerCountry());
    entity.setPartnerIsoCode(domain.isoCode().isoCode());
    entity.setPartnerStatus(domain.status().name());
    entity.setPartnerCreatedAt(domain.createdAt().createdAt());
    entity.setPartnerUpdatedAt(domain.updateAt().updatedAt());
    return entity;
  }

  public static Partner toDomain(PartnerEntity entity) {
    return new Partner(
        new PartnerId(entity.getPartnerId()),
        new PartnerName(entity.getPartnerName()),
        new PartnerCity(entity.getPartnerCity()),
        new PartnerCountry(entity.getPartnerCountry()),
        new PartnerIsoCode(entity.getPartnerIsoCode()),
        PartnerStatus.valueOf(entity.getPartnerStatus()),
        new PartnerCreatedAt(entity.getPartnerCreatedAt()),
        new PartnerUpdateAt(entity.getPartnerUpdatedAt()));
  }
}
