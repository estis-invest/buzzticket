package com.efpcode.application.usecase.partner.dto;

import com.efpcode.application.usecase.partner.exceptions.InvalidPartnerCommandArgumentException;

public record UpdatePartnerCommand(String name, String city, String country, String isoCode) {
  public UpdatePartnerCommand {
    validateRequiredFields(name, "Name");
    validateRequiredFields(city, "City");
    validateRequiredFields(country, "Country");
    validateRequiredFields(isoCode, "IsoCode");
  }

  private void validateRequiredFields(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new InvalidPartnerCommandArgumentException(
          fieldName + " is required and cannot be null or blank");
    }
  }
}
