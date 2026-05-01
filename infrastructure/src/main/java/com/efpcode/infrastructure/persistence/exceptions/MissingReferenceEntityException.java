package com.efpcode.infrastructure.persistence.exceptions;

public class MissingReferenceEntityException extends InfrastructureLayerException {
  public MissingReferenceEntityException(String entityName, Object identifier) {
    super(
        String.format(
            "%application.yml not found for id: %application.yml", entityName, identifier));
  }
}
