package com.efpcode.domain.partner.model;

import com.efpcode.domain.partner.exceptions.IllegalPartnerIsoCodeFormatException;
import com.efpcode.domain.partner.exceptions.InvalidPartnerIsoCodeException;

public record PartnerIsoCode(String isoCode) {
  public PartnerIsoCode {
    if (isoCode == null || isoCode.isBlank())
      throw new InvalidPartnerIsoCodeException("IsoCode cannot be null or blank");

    isoCode = isoCode.trim();

    if (!isoCode.equals(isoCode.toUpperCase()))
      throw new IllegalPartnerIsoCodeFormatException("IsoCode must be uppercase");

    if (isoCode.length() != 3)
      throw new IllegalPartnerIsoCodeFormatException(
          "IsoCode must be 3 characters long and letters need to be uppercase");
  }

  public PartnerIsoCode update(String newIsoCode) {
    return new PartnerIsoCode(newIsoCode);
  }
}
