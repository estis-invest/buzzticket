package com.efpcode.infrastructure.persistence.exceptions;

public abstract class InfrastructureLayerException extends RuntimeException {
  protected InfrastructureLayerException(String message, Throwable cause) {
    super(message, cause);
  }
}
