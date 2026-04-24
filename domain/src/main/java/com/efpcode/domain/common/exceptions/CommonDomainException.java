package com.efpcode.domain.common.exceptions;

public abstract class CommonDomainException extends RuntimeException {
  protected CommonDomainException(String message) {
    super(message);
  }
}
