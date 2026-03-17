package com.efpcode.application.usecase.partner.exceptions;

public abstract class PartnerApplicationException extends RuntimeException {
  protected PartnerApplicationException(String message) {
    super(message);
  }
}
