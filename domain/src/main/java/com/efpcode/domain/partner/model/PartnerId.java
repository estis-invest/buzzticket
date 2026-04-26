package com.efpcode.domain.partner.model;

import com.efpcode.domain.partner.exceptions.IllegalPartnerIdArgumentException;
import com.efpcode.domain.partner.exceptions.InvalidPartnerIdException;
import java.util.UUID;

public record PartnerId(UUID partnerId) {

  public PartnerId {
    if (partnerId == null) throw new InvalidPartnerIdException("Partner cannot be null");
  }

  public static PartnerId of(UUID value) {
    return new PartnerId(value);
  }

  public static PartnerId fromString(String uuid) {
    if (uuid == null || uuid.trim().isBlank())
      throw new IllegalPartnerIdArgumentException("fromString method cannot pass null or blank");

    return new PartnerId(UUID.fromString(uuid));
  }
}
