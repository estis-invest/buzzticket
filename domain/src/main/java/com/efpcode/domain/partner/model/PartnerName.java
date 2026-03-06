package com.efpcode.domain.partner.model;

import com.efpcode.domain.partner.exceptions.IllegalPartnerNameLengthException;
import com.efpcode.domain.partner.exceptions.InvalidPartnerNameException;

public record PartnerName(String partnerName) {

  private static final int CHARACTER_LIMIT = 255;

  public PartnerName {
    if (partnerName == null || partnerName.isBlank())
      throw new InvalidPartnerNameException(
          "PartnerName cannot be instantiated with null or blank as argument.");

    if (partnerName.trim().length() > CHARACTER_LIMIT)
      throw new IllegalPartnerNameLengthException(
          "PartnerName must not exceed character limit: " + CHARACTER_LIMIT);
  }

  public PartnerName update(String partnerName) {
    return new PartnerName(partnerName);
  }
}
