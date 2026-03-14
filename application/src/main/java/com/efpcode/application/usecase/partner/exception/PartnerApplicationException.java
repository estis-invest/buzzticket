package com.efpcode.application.usecase.partner.exception;

public abstract class PartnerApplicationException extends RuntimeException {
  protected PartnerApplicationException(String message) {
    super(message);
  }
}
