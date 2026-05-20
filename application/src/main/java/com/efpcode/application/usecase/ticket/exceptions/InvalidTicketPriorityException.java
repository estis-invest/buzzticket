package com.efpcode.application.usecase.ticket.exceptions;

public class InvalidTicketPriorityException extends TicketApplicationException {
  public InvalidTicketPriorityException(String message) {
    super(message);
  }
}
