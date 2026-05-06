package com.efpcode.infrastructure.persistence.exceptions;

public class MissingReferenceEntityException extends InfrastructureLayerException {
  public MissingReferenceEntityException(String entityName, Object identifier, Throwable cause) {
    super(
        cause == null
            ? entityName + ": " + identifier
            : entityName + ": " + identifier + ": " + cause.getMessage(),
        cause);
  }
}
