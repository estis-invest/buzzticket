package com.efpcode.domain.ticket.model;

import com.efpcode.domain.ticket.exceptions.InvalidTicketSlugException;
import com.efpcode.domain.ticket.exceptions.TicketSlugFormatException;
import com.efpcode.domain.ticket.exceptions.TicketSlugLengthException;
import java.util.regex.Pattern;

public record TicketSlug(String slug) {
  private static final Pattern FORMAT = Pattern.compile("^[A-Z]{3}-\\d+$");

  public TicketSlug {

    if (slug == null || slug.isBlank()) {
      throw new InvalidTicketSlugException("Slug cannot be null or blank");
    }
    slug = slug.trim();

    if (slug.length() > 64) {
      throw new TicketSlugLengthException("Slug length is greater than max range");
    }

    if (!FORMAT.matcher(slug).matches()) {
      throw new TicketSlugFormatException("Slug must follow format `AAA-0000...000`");
    }
  }
}
