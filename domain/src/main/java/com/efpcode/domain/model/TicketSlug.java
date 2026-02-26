package com.efpcode.domain.model;

import java.util.regex.Pattern;

public record TicketSlug(String slug) {
  private static final Pattern FORMAT = Pattern.compile("^[A-Z]{3}-\\d+$");

  public TicketSlug {

    if (slug == null || slug.isBlank()) {
      throw new IllegalArgumentException("Slug cannot be null or blank");
    }

    if (slug.length() > 64) {
      throw new IllegalArgumentException("Slug length is greater than max range");
    }

    if (!FORMAT.matcher(slug).matches()) {
      throw new IllegalArgumentException("Slug must follow format `AAA-0000`");
    }
  }
}
