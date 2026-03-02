package com.efpcode.domain.ticket.exceptions;

public class InvalidTicketDescriptionException extends TicketDomainException {
  public InvalidTicketDescriptionException(String message) {
    super(message);
  }
}
