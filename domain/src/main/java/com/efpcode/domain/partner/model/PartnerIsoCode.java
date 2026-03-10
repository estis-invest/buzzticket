package com.efpcode.domain.partner.model;

import com.efpcode.domain.partner.exceptions.IllegalPartnerIsoCodeFormatException;
import com.efpcode.domain.partner.exceptions.InvalidPartnerIsoCodeException;
import java.util.regex.Pattern;

public record PartnerIsoCode(String isoCode) {
  private static final Pattern FORMAT = Pattern.compile("^[A-Z]{3}$");

  public PartnerIsoCode {
    if (isoCode == null || isoCode.isBlank())
      throw new InvalidPartnerIsoCodeException("IsoCode cannot be null or blank");

    isoCode = isoCode.trim();

    if (!FORMAT.matcher(isoCode).matches())
      throw new IllegalPartnerIsoCodeFormatException(
          "IsoCode must be exactly 3 characters long and only contain letters in uppercase");
  }

  public PartnerIsoCode update(String newIsoCode) {
    return new PartnerIsoCode(newIsoCode);
  }
}
