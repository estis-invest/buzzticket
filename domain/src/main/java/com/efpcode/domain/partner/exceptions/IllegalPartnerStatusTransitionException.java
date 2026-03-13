package com.efpcode.domain.partner.exceptions;

public class IllegalPartnerStatusTransitionException extends PartnerDomainException {
  public IllegalPartnerStatusTransitionException(String message) {
    super(message);
  }
}
