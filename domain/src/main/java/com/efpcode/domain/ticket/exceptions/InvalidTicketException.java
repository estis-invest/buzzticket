package com.efpcode.domain.ticket.exceptions;

public class InvalidTicketException extends TicketDomainException {
  public InvalidTicketException(String message) {
    super(message);
  }
}
