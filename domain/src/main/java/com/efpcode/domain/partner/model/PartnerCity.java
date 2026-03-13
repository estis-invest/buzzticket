package com.efpcode.domain.partner.model;

import com.efpcode.domain.partner.exceptions.IllegalPartnerCityMaxLengthException;
import com.efpcode.domain.partner.exceptions.IllegalPartnerCityNameFormatException;
import com.efpcode.domain.partner.exceptions.InvalidPartnerCityException;
import java.text.Normalizer;
import java.util.regex.Pattern;

public record PartnerCity(String partnerCity) {

  private static final int MAX_LENGTH = 100;
  private static final Pattern FORMAT = Pattern.compile("^[0-9\\p{L}\\p{M} .'-]+$");

  public PartnerCity {
    if (partnerCity == null || partnerCity.isBlank())
      throw new InvalidPartnerCityException("cannot pass null or blank as partner city");

    partnerCity = partnerCity.trim();
    partnerCity = Normalizer.normalize(partnerCity, Normalizer.Form.NFC);

    if (partnerCity.length() > MAX_LENGTH)
      throw new IllegalPartnerCityMaxLengthException(
          String.format(
              "City name cannot exceed %s characters, input length was: %s",
              MAX_LENGTH, partnerCity.length()));

    if (!FORMAT.matcher(partnerCity).matches())
      throw new IllegalPartnerCityNameFormatException(
          "City name should only contain letters, digits, dots, hyphen, apostrophe and space");
  }
}
