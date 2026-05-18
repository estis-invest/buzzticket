package com.efpcode.domain.ticket.model;

import com.efpcode.domain.ticket.exceptions.InvalidTicketUpdateAtException;
import java.time.Instant;

public record TicketUpdateAt(Instant updatedAt) {

  public TicketUpdateAt {
    if (updatedAt == null || updatedAt.equals(Instant.EPOCH)) {
      throw new InvalidTicketUpdateAtException("Update timestamp is required");
    }
  }

  public static TicketUpdateAt of(Instant time) {
    return new TicketUpdateAt(time);
  }
}
