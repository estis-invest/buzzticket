package com.efpcode.domain.partner.model;

import com.efpcode.domain.partner.exceptions.InvalidPartnerCreatedAtException;
import com.efpcode.domain.partner.exceptions.PartnerCreatedAtDateException;
import java.time.Instant;

public record PartnerCreatedAt(Instant createdAt) {
  private static final int GRACE_PERIOD = 60;

  public PartnerCreatedAt {
    if (createdAt == null || createdAt.equals(Instant.ofEpochMilli(0)))
      throw new InvalidPartnerCreatedAtException("Time is required");

    if (createdAt.isAfter(Instant.now().plusSeconds(GRACE_PERIOD)))
      throw new PartnerCreatedAtDateException("Partner cannot be created in the future");
  }

  public static PartnerCreatedAt createNow() {
    return new PartnerCreatedAt(Instant.now());
  }
}
