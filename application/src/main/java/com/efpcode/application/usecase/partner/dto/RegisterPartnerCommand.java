package com.efpcode.application.usecase.partner.dto;

import com.efpcode.application.usecase.partner.exceptions.InvalidRegisterPartnerCommandArgumentException;

public record RegisterPartnerCommand(String name, String city, String country, String isoCode) {

  public RegisterPartnerCommand {
    validateRequiredFields(name, "Name");
    validateRequiredFields(city, "City");
    validateRequiredFields(country, "Country");
    validateRequiredFields(isoCode, "IsoCode");
  }

  private void validateRequiredFields(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new InvalidRegisterPartnerCommandArgumentException(
          fieldName + " is required cannot be null or blank");
    }
  }
}
