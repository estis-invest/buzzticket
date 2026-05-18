package com.efpcode.domain.ticket.exceptions;

public class IllegalTicketDescriptionUpdateException extends TicketDomainException {
  public IllegalTicketDescriptionUpdateException(String message) {
    super(message);
  }
}
