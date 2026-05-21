package com.efpcode.application.usecase.ticket.exceptions;

public class InvalidTicketStatusTransitionException extends TicketApplicationException {
  public InvalidTicketStatusTransitionException(String message) {
    super(message);
  }
}
