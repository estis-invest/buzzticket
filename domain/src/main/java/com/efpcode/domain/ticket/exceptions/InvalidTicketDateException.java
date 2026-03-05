package com.efpcode.domain.ticket.exceptions;

public class InvalidTicketDateException extends TicketDomainException {
  public InvalidTicketDateException(String message) {
    super(message);
  }
}
