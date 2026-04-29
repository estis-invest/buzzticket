package com.efpcode.infrastructure.security.exceptions;

import com.efpcode.infrastructure.persistence.exceptions.InfrastructureLayerException;

public class InvalidTokenException extends InfrastructureLayerException {
  public InvalidTokenException(String message) {
    super(message);
  }
}
