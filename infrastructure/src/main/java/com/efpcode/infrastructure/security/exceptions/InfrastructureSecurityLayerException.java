package com.efpcode.infrastructure.security.exceptions;

public abstract class InfrastructureSecurityLayerException extends RuntimeException {
  protected InfrastructureSecurityLayerException(String message, Throwable cause) {
    super(message, cause);
  }
}
