package com.efpcode.infrastructure.persistence.partner.exceptions;

public abstract class PartnerInfrastructureException extends RuntimeException {
  protected PartnerInfrastructureException(String message) {
    super(message);
  }
}
