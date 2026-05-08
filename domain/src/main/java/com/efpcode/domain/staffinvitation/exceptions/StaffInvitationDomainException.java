package com.efpcode.domain.staffinvitation.exceptions;

public abstract class StaffInvitationDomainException extends RuntimeException {
    protected StaffInvitationDomainException(String message) {
        super(message);
    }
}
