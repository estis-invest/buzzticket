package com.efpcode.domain.partner.model;

import com.efpcode.domain.partner.exceptions.InvalidPartnerUpdateAtException;
import com.efpcode.domain.partner.exceptions.PartnerUpdateAtDateException;
import java.time.Instant;

public record PartnerUpdateAt(Instant updatedAt) {

  private static final int GRACE_PERIOD = 60;

  public PartnerUpdateAt {
    if (updatedAt == null || updatedAt.equals(Instant.ofEpochMilli(0))) {
      throw new InvalidPartnerUpdateAtException("Update timestamp is required");
    }

    if (updatedAt.isAfter(Instant.now().plusSeconds(GRACE_PERIOD))) {
      throw new PartnerUpdateAtDateException("Update timestamp cannot be in the future");
    }
  }

  public static PartnerUpdateAt createNow() {
    return new PartnerUpdateAt(Instant.now());
  }
}
