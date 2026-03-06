package com.efpcode.domain.ticket.exceptions;

public class IllegalTicketStatusTransitionException extends TicketDomainException {
  public IllegalTicketStatusTransitionException(String message) {
    super(message);
  }
}
