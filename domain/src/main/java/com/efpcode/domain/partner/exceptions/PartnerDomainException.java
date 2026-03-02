package com.efpcode.domain.partner.exceptions;

public abstract class PartnerDomainException extends RuntimeException {
  protected PartnerDomainException(String message) {
    super(message);
  }
}
