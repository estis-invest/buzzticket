package com.efpcode.domain.ticket.exceptions;

public class InvalidCreatedAtException extends TicketDomainException {
  public InvalidCreatedAtException(String message) {
    super(message);
  }
}
