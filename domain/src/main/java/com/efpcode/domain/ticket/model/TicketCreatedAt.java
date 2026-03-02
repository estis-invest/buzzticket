package com.efpcode.domain.ticket.model;

import com.efpcode.domain.ticket.exceptions.InvalidCreatedAtException;
import com.efpcode.domain.ticket.exceptions.InvalidTicketDateException;
import java.time.Instant;

public record TicketCreatedAt(Instant time) {
  private static final int GRACE_PERIOD = 60;

  public TicketCreatedAt {
    if (time == null || time.equals(Instant.ofEpochMilli(0)))
      throw new InvalidCreatedAtException("Time is required");

    if (time.isAfter(Instant.now().plusSeconds(GRACE_PERIOD)))
      throw new InvalidTicketDateException("Time cannot be created in the future");
  }

  public static TicketCreatedAt createNow() {
    return new TicketCreatedAt(Instant.now());
  }
}
