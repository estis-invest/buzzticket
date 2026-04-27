package com.efpcode.domain.ticket.model;

import com.efpcode.domain.ticket.exceptions.InvalidTicketUpdateAtException;
import com.efpcode.domain.ticket.exceptions.TicketUpdateAtDateException;
import java.time.Instant;

public record TicketUpdateAt(Instant updatedAt) {
  private static final int GRACE_PERIOD = 60;

  public TicketUpdateAt {
    if (updatedAt == null || updatedAt.equals(Instant.ofEpochMilli(0))) {
      throw new InvalidTicketUpdateAtException("Update timestamp is required");
    }

    if (updatedAt.isAfter(Instant.now().plusSeconds(GRACE_PERIOD))) {
      throw new TicketUpdateAtDateException("Update timestamp cannot be in the future");
    }
  }

  public static TicketUpdateAt createNow() {
    return new TicketUpdateAt(Instant.now());
  }
}
