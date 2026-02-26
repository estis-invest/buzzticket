package com.efpcode.domain.model;

public record TicketDescription(String description) {

  public TicketDescription {
    if (description == null || description.isBlank()) {
      throw new IllegalArgumentException("Description cannot be empty");
    }

    if (description.length() > 1800) {
      throw new IllegalArgumentException("Description has surpass max length");
    }
  }
}
