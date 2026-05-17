package com.efpcode.application.usecase.user.exceptions;

public class PartnerRequiresAdminException extends UserApplicationException {
  public PartnerRequiresAdminException(String message) {
    super(message);
  }
}
