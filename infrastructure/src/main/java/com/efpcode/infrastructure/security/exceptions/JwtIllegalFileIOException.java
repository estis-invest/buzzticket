package com.efpcode.infrastructure.security.exceptions;

public class JwtIllegalFileIOException extends InfrastructureSecurityLayerException {
  public JwtIllegalFileIOException(String message, Throwable cause) {
    super(cause == null ? message : message + ": " + cause.getMessage(), cause);
  }
}
