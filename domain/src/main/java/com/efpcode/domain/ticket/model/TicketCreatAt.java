package com.efpcode.domain.ticket.model;

import java.time.Instant;

public record TicketCreatAt(Instant time) {
  private static final int GRACE_PERIOD = 60;

  public TicketCreatAt {
    if (time == null || time.equals(Instant.ofEpochMilli(0)))
      throw new IllegalArgumentException("Time is required");

    if (time.isAfter(Instant.now().plusSeconds(GRACE_PERIOD)))
      throw new IllegalArgumentException("Time cannot be created in the future");
  }

  public static TicketCreatAt createNow() {
    return new TicketCreatAt(Instant.now());
  }
}
