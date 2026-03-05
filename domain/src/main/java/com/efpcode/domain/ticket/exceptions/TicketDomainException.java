package com.efpcode.domain.ticket.exceptions;

public abstract class TicketDomainException extends RuntimeException {
  protected TicketDomainException(String message) {
    super(message);
  }
}
