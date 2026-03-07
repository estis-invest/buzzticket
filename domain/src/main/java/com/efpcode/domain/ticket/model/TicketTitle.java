package com.efpcode.domain.ticket.model;

import com.efpcode.domain.ticket.exceptions.InvalidTicketTitleException;
import com.efpcode.domain.ticket.exceptions.TicketTitleLengthException;

public record TicketTitle(String title) {

  public TicketTitle {

    if (title == null || title.isBlank())
      throw new InvalidTicketTitleException("Title cannot be blank or null");

    title = title.trim();

    if (title.length() > 50) {
      throw new TicketTitleLengthException("Max length of title reached");
    }
  }
}
