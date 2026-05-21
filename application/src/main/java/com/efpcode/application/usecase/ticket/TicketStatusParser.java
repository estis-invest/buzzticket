package com.efpcode.application.usecase.ticket;

import com.efpcode.domain.ticket.exceptions.IllegalTicketStatusTransitionException;
import com.efpcode.domain.ticket.model.TicketStatus;
import java.util.Locale;

final class TicketStatusParser {
  private TicketStatusParser() {}

  static TicketStatus parse(String value) {
    if (value == null || value.trim().isBlank()) {
      throw new IllegalTicketStatusTransitionException("Ticket status required");
    }

    try {
      return TicketStatus.valueOf(value.trim().toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException ex) {
      throw new IllegalTicketStatusTransitionException("Invalid ticket status: " + value);
    }
  }
}
