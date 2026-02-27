package com.efpcode.domain.ticket.model;

public record TicketTitle(String title) {

  public TicketTitle {

    if (title == null || title.isBlank())
      throw new IllegalArgumentException("Title cannot be blank or null");

    if (title.length() > 50) {
      throw new IllegalArgumentException("Max length of title reached");
    }
  }
}
