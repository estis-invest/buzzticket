package com.efpcode.domain.model;

import java.time.Instant;

public record TicketCreatAt(Instant time) {

  public TicketCreatAt {
    if (time == null) throw new IllegalArgumentException("Time is required");
  }
}
