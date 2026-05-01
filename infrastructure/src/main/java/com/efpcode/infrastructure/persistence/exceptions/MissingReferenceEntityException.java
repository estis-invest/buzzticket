package com.efpcode.infrastructure.persistence.exceptions;

public class MissingReferenceEntityException extends InfrastructureLayerException {
  public MissingReferenceEntityException(String entityName, Object identifier) {
    super(
        String.format(
            "%s application.yml not found for id: %s application.yml", entityName, identifier));
  }
}
