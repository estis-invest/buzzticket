package com.efpcode.domain.ticket.exceptions;

public class InvalidTicketTitleException extends TicketDomainException {
  public InvalidTicketTitleException(String message) {
    super(message);
  }
}
