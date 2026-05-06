package com.efpcode.infrastructure.security.exceptions;

public class JwtTokenRequiredFieldMissingException extends InfrastructureSecurityLayerException {
  public JwtTokenRequiredFieldMissingException(String message, Throwable cause) {
    super(cause == null ? message : message + ": " + cause.getMessage(), cause);
  }
}
