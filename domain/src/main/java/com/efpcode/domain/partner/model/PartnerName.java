package com.efpcode.domain.partner.model;

import com.efpcode.domain.partner.exceptions.IllegalPartnerNameFormatException;
import com.efpcode.domain.partner.exceptions.IllegalPartnerNameLengthException;
import com.efpcode.domain.partner.exceptions.InvalidPartnerNameException;
import java.util.regex.Pattern;

public record PartnerName(String partnerName) {

  private static final int CHARACTER_LIMIT = 255;
  private static final Pattern FORMAT = Pattern.compile("^[0-9\\p{L} .'&,()@-]+$");

  public PartnerName {
    if (partnerName == null || partnerName.isBlank())
      throw new InvalidPartnerNameException(
          "PartnerName cannot be instantiated with null or blank as argument.");

    partnerName = partnerName.trim();

    if (partnerName.length() > CHARACTER_LIMIT)
      throw new IllegalPartnerNameLengthException(
          "PartnerName must not exceed character limit: " + CHARACTER_LIMIT);

    if (!FORMAT.matcher(partnerName).matches())
      throw new IllegalPartnerNameFormatException(
          String.format(
              "Partner name: %s is not following format standard allowed characters are letters, digits, ampersands, apostrophe, hyphen, dots, @, parenthesis and commas",
              partnerName));
  }

  public PartnerName update(String partnerName) {
    return new PartnerName(partnerName);
  }
}
