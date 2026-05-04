package com.efpcode.infrastructure.security.exceptions;

import com.efpcode.infrastructure.persistence.exceptions.InfrastructureLayerException;

public class JwtIllegalFileIOException extends InfrastructureLayerException {
  public JwtIllegalFileIOException(String message, Exception e) {
    super(String.format("%s :%s", message, e));
  }
}
