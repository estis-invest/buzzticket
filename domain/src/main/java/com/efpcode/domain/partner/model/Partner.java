package com.efpcode.domain.partner.model;

import java.util.Objects;

public record Partner(
    PartnerId id,
    PartnerName name,
    PartnerCity city,
    PartnerCountry country,
    PartnerIsoCode isoCode,
    PartnerStatus status) {

  public Partner {
    Objects.requireNonNull(id, "Id cannot be null");
    Objects.requireNonNull(name, "Name cannot be null");
    Objects.requireNonNull(city, "City cannot be null");
    Objects.requireNonNull(country, "Country cannot be null");
    Objects.requireNonNull(isoCode, "IsoCode cannot be null");
    Objects.requireNonNull(status, "Status cannot be null");
  }
}
