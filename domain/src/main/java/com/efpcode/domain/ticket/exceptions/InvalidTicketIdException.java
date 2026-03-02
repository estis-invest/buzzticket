package com.efpcode.domain.ticket.exceptions;

public class InvalidTicketIdException extends TicketDomainException {
  public InvalidTicketIdException(String message) {
    super(message);
  }
}
