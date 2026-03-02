package com.efpcode.domain.ticket.exceptions;

public class MissingUserAssignmentException extends TicketDomainException {
  public MissingUserAssignmentException(String message) {
    super(message);
  }
}
