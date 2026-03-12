package com.efpcode.domain.partner.model;

import com.efpcode.domain.partner.exceptions.IllegalPartnerCountryFormatException;
import com.efpcode.domain.partner.exceptions.IllegalPartnerCountryMaxLengthException;
import com.efpcode.domain.partner.exceptions.InvalidPartnerCountryException;
import java.util.Arrays;
import java.util.regex.Pattern;

public record PartnerCountry(String partnerCountry) {
  private static final Pattern FORMAT = Pattern.compile("^\\p{Lu}[\\p{Lu}\\s.',() -]*[\\p{Lu}).]$");
  private static final int MAX_LENGTH = 100;
  private static final int MINIMUM_LENGTH = 2;

  public PartnerCountry {
    if (partnerCountry == null || partnerCountry.isBlank())
      throw new InvalidPartnerCountryException("Country cannot be created with null or blank");

    partnerCountry = partnerCountry.trim();

    boolean hasValidWords =
        Arrays.stream(partnerCountry.split("\\s+"))
            .allMatch(word -> word.length() >= MINIMUM_LENGTH);

    if (!hasValidWords)
      throw new IllegalPartnerCountryFormatException(
          String.format("Country cannot be created with invalid words: %s", partnerCountry));

    if (!FORMAT.matcher(partnerCountry).matches())
      throw new IllegalPartnerCountryFormatException(
          String.format(
              "Country name: %s does not follow format: only upper case letters, commas, parenthesis, hyphens and dots are allowed",
              partnerCountry));

    if (partnerCountry.length() > MAX_LENGTH)
      throw new IllegalPartnerCountryMaxLengthException(
          String.format(
              "Country name passed exceeds max length: %d characters. Passed name had a length of %d characters",
              MAX_LENGTH, partnerCountry.length()));
  }
}
