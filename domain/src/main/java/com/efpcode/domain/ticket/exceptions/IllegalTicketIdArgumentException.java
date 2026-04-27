package com.efpcode.domain.ticket.exceptions;

public class IllegalTicketIdArgumentException extends TicketDomainException {
  public IllegalTicketIdArgumentException(String message) {
    super(message);
  }
}
