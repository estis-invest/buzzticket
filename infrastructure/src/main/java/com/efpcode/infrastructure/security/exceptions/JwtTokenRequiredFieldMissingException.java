package com.efpcode.infrastructure.security.exceptions;

import com.efpcode.infrastructure.persistence.exceptions.InfrastructureLayerException;

public class JwtTokenRequiredFieldMissingException extends InfrastructureLayerException {
  public JwtTokenRequiredFieldMissingException(String message) {
    super(message);
  }
}
