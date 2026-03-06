package com.efpcode.domain.ticket.exceptions;

public class IllegalTicketStatusAssignmentException extends TicketDomainException {
  public IllegalTicketStatusAssignmentException(String message) {
    super(message);
  }
}
