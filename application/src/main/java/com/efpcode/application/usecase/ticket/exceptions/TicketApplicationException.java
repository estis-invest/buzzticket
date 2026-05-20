package com.efpcode.application.usecase.ticket.exceptions;

public abstract class TicketApplicationException extends RuntimeException {
    protected TicketApplicationException(String message) {
        super(message);
    }
}
