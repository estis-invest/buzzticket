package com.efpcode.application.usecase.ticket.exceptions;

public class InvalidTicketAuthorizationException extends TicketApplicationException {
  public InvalidTicketAuthorizationException(String message) {
    super(message);
  }
}
