package com.efpcode.infrastructure.web.dto;

import com.efpcode.application.usecase.partner.exceptions.InvalidPartnerCommandArgumentException;

public record UpdatePartnerRequest(String name, String city, String country, String isoCode) {
  public UpdatePartnerRequest {
    if (hasNoData(name) && hasNoData(city) && hasNoData(country) && hasNoData(isoCode)) {
      throw new InvalidPartnerCommandArgumentException(
          "At least one field must be provided for an update.");
    }
  }

  public String getNormalizedName() {
    return normalize(name);
  }

  public String getNormalizedCity() {
    return normalize(city);
  }

  public String getNormalizedCountry() {
    return normalize(country);
  }

  public String getNormalizedIsoCode() {
    return normalize(isoCode);
  }

  private String normalize(String value) {
    return (value == null || value.isBlank()) ? null : value;
  }

  private boolean hasNoData(String value) {
    return value == null || value.isBlank();
  }
}
