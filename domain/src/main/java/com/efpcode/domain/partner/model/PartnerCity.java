package com.efpcode.domain.partner.model;

import com.efpcode.domain.partner.exceptions.IllegalPartnerCityMaxLengthException;
import com.efpcode.domain.partner.exceptions.IllegalPartnerCityNameFormatException;
import com.efpcode.domain.partner.exceptions.InvalidPartnerCityException;
import java.util.regex.Pattern;

public record PartnerCity(String partnerCity) {

  private static final int MAX_LENGTH = 100;
  private static final Pattern FORMAT = Pattern.compile("^[0-9\\p{L}\\s.'-]+$");

  public PartnerCity {
    if (partnerCity == null || partnerCity.isBlank())
      throw new InvalidPartnerCityException("cannot pass null or blank as partner city");

    partnerCity = partnerCity.trim();

    if (partnerCity.length() > MAX_LENGTH)
      throw new IllegalPartnerCityMaxLengthException(
          String.format(
              "City name cannot exceed %s characters, input was: %s",
              MAX_LENGTH, partnerCity.length()));

    if (!FORMAT.matcher(partnerCity).matches())
      throw new IllegalPartnerCityNameFormatException(
          "City name should only contain letters, dots, hyphen, apostrophe and space");
  }

  public PartnerCity update(String newCity) {
    return new PartnerCity(newCity);
  }
}
