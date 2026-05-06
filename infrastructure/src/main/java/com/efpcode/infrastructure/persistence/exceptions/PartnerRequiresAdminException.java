package com.efpcode.infrastructure.persistence.exceptions;

public class PartnerRequiresAdminException extends InfrastructureLayerException {
  public PartnerRequiresAdminException(String message, Throwable cause) {
    super(cause == null ? message : message + ": " + cause.getMessage(), cause);
  }
}
