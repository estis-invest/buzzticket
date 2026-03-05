package com.efpcode.domain.ticket.model;

import com.efpcode.domain.ticket.exceptions.InvalidTicketDescriptionException;
import com.efpcode.domain.ticket.exceptions.TicketDescriptionLengthException;

public record TicketDescription(String description) {
  private static final int MAX_CHARACTER_LENGTH = 1800;

  public TicketDescription {
    if (description == null || description.isBlank()) {
      throw new InvalidTicketDescriptionException("Description cannot be empty");
    }

    if (description.length() > MAX_CHARACTER_LENGTH) {
      throw new TicketDescriptionLengthException(
          "Description exceeds max length of " + MAX_CHARACTER_LENGTH + " characters");
    }
  }
}
