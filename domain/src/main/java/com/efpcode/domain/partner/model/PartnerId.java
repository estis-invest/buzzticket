package com.efpcode.domain.partner.model;

import com.efpcode.domain.partner.exceptions.IllegalPartnerIdArgumentException;
import com.efpcode.domain.partner.exceptions.InvalidPartnerIdException;
import java.util.UUID;

public record PartnerId(UUID partnerId) {

  public PartnerId {
    if (partnerId == null) throw new InvalidPartnerIdException("Partner cannot be null");
  }

  public static PartnerId generate() {
    return new PartnerId(UUID.randomUUID());
  }

  public static PartnerId fromString(String uuid) {
    if (uuid == null || uuid.isBlank())
      throw new IllegalPartnerIdArgumentException("fromString method cannot pass null or blank");

    return new PartnerId(UUID.fromString(uuid));
  }
}
