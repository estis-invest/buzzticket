package com.efpcode.domain.common.exceptions;

public class InvalidCommonPasswordException extends RuntimeException {
  public InvalidCommonPasswordException(String message) {
    super(message);
  }
}
