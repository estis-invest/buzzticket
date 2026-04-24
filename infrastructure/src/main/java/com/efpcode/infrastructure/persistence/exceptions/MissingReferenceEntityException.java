package com.efpcode.infrastructure.persistence.exceptions;

public class MissingReferenceEntityException extends InfrastructureLayerException {
  public MissingReferenceEntityException(String entityName, Object identifier) {
    super(String.format("%s not found for id: %s", entityName, identifier));
  }
}
