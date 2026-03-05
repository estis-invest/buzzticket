package com.efpcode.domain.partner.model;

import com.efpcode.domain.partner.exceptions.InvalidPartnerIdException;
import java.util.UUID;

public record PartnerId(UUID partnerId) {
  private static final UUID EMPTY_UUID = new UUID(0L, 0L);

  public PartnerId {
    if (partnerId == null) throw new InvalidPartnerIdException("Partner cannot be null");
  }

  public static PartnerId generate() {
    return new PartnerId(UUID.randomUUID());
  }

  public static PartnerId fromString(String uuid) {
    return new PartnerId(UUID.fromString(uuid));
  }
}
