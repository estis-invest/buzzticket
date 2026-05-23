package com.efpcode.application.usecase.ticket.exceptions;

public class InvalidTicketAssignmentException extends TicketApplicationException {
  public InvalidTicketAssignmentException(String message) {
    super(message);
  }
}
