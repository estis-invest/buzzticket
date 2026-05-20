package com.efpcode.application.usecase.ticket.exceptions;

public class IllegalTicketSlugException extends TicketApplicationException {
    public IllegalTicketSlugException(String message){
        super(message);
    }
}
