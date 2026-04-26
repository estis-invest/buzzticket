package com.efpcode.domain.ticket.model;

import com.efpcode.domain.ticket.exceptions.InvalidTicketIdException;
import java.util.UUID;

public record TicketId(UUID value) {

  public TicketId {
    if (value == null) {
      throw new InvalidTicketIdException("UUID is required and cannot be null");
    }
  }

  public static TicketId of(UUID value) {
    return new TicketId(value);
  }

  public static TicketId fromString(String uuid) {
    return new TicketId(UUID.fromString(uuid));
  }
}
