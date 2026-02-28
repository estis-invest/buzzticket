package com.efpcode.domain.ticket.model;

import java.util.UUID;

public record TicketId(UUID value) {

  public TicketId {
    if (value == null) {
      throw new IllegalArgumentException("UUID is required and cannot be null");
    }
  }

  public static TicketId generate() {
    return new TicketId(UUID.randomUUID());
  }

  public static TicketId fromString(String uuid) {
    return new TicketId(UUID.fromString(uuid));
  }
}
