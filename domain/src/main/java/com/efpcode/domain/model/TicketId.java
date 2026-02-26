package com.efpcode.domain.model;

import java.util.UUID;

public record TicketId(UUID value) {

  public TicketId {
    if (value == null) {
      throw new IllegalArgumentException("UUID is required and cannot be null");
    }
  }
}
