package com.efpcode.application.usecase.ticket.exceptions;

public class InvalidTicketNotFoundException extends TicketApplicationException {

  public InvalidTicketNotFoundException(String message) {
    super(message);
  }
}
