package com.efpcode.domain.ticket.model;

public record TicketDescription(String description) {
  private static final int MAX_CHARACTER_LENGTH = 1800;

  public TicketDescription {
    if (description == null || description.isBlank()) {
      throw new IllegalArgumentException("Description cannot be empty");
    }

    if (description.length() > MAX_CHARACTER_LENGTH) {
      throw new IllegalArgumentException("Description has surpass max length");
    }
  }
}
