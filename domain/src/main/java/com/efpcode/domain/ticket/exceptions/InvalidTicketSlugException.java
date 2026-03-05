package com.efpcode.domain.ticket.exceptions;

public class InvalidTicketSlugException extends TicketDomainException {
  public InvalidTicketSlugException(String message) {
    super(message);
  }
}
