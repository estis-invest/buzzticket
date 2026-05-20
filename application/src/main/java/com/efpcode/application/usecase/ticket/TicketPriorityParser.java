package com.efpcode.application.usecase.ticket;

import com.efpcode.application.usecase.ticket.exceptions.InvalidTicketPriorityException;
import com.efpcode.domain.ticket.model.TicketPriority;
import java.util.Locale;

final class TicketPriorityParser {
  private TicketPriorityParser() {}

  static TicketPriority parse(String value) {
    if (value == null || value.trim().isBlank()) {
      throw new InvalidTicketPriorityException("Ticket priority required");
    }

    try {
      return TicketPriority.valueOf(value.trim().toUpperCase(Locale.ROOT));

    } catch (IllegalArgumentException ex) {
      throw new InvalidTicketPriorityException("Invalid ticket priority: " + value);
    }
  }
}
