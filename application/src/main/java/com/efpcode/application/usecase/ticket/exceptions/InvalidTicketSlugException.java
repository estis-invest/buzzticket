package com.efpcode.application.usecase.ticket.exceptions;

public class InvalidTicketSlugException extends TicketApplicationException {
  public InvalidTicketSlugException(String message) {
    super(message);
  }
}
