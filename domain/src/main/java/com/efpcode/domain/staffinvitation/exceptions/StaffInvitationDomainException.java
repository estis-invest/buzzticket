package com.efpcode.domain.staffinvitation.exceptions;

public abstract class StaffInvitationDomainException extends RuntimeException {
  protected StaffInvitationDomainException(String message, Throwable cause) {
    super(cause == null ? message : message + ": " + cause.getMessage(), cause);
  }
}
