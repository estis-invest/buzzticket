package com.efpcode.infrastructure.security.exceptions;

import com.efpcode.infrastructure.persistence.exceptions.InfrastructureLayerException;

public class BootstrapNotAllowedException extends InfrastructureLayerException {
  public BootstrapNotAllowedException(String message) {
    super(message);
  }
}
