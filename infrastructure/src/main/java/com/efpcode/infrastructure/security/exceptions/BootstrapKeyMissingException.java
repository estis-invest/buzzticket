package com.efpcode.infrastructure.security.exceptions;

import com.efpcode.infrastructure.persistence.exceptions.InfrastructureLayerException;

public class BootstrapKeyMissingException extends InfrastructureLayerException {
  public BootstrapKeyMissingException(String message) {
    super(message);
  }
}
