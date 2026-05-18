package com.efpcode.domain.ticket.model;

import com.efpcode.domain.ticket.exceptions.InvalidCreatedAtException;
import java.time.Instant;

public record TicketCreatedAt(Instant time) {
  public TicketCreatedAt {
    if (time == null || time.equals(Instant.EPOCH))
      throw new InvalidCreatedAtException("Time is required");
  }

  public static TicketCreatedAt of(Instant time) {
    return new TicketCreatedAt(time);
  }
}
