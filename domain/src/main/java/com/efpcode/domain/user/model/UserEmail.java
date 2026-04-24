package com.efpcode.domain.user.model;

import com.efpcode.domain.user.exceptions.InvalidUserEmailException;
import com.efpcode.domain.user.exceptions.UserEmailFormatException;
import com.efpcode.domain.user.exceptions.UserEmailLengthException;
import java.util.Locale;
import java.util.regex.Pattern;

public record UserEmail(String email) {
  private static final Pattern FORMAT =
      Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
  private static final String EXAMPLE = "user@domain.io";
  private static final int EMAIL_OCTETS_LIMIT = 254;
  private static final int DOMAIN_OCTETS_LIMIT = 255;
  private static final int LOCAL_OCTETS_LIMIT = 64;

  public UserEmail {
    if (email == null || email.trim().isBlank())
      throw new InvalidUserEmailException("User email cannot be null or blank");

    String validate = email.trim();

    if (!FORMAT.matcher(validate).matches())
      throw new UserEmailFormatException(
          "Email has not a valid format expected format: " + EXAMPLE);

    int lastAtIndex = validate.lastIndexOf("@");
    String localPart = validate.substring(0, lastAtIndex);
    String domainPart = validate.substring(lastAtIndex + 1);

    if (localPart.length() > LOCAL_OCTETS_LIMIT)
      throw new UserEmailLengthException(
          "Local-part (before @) exceeds " + LOCAL_OCTETS_LIMIT + " characters");

    if (domainPart.length() > DOMAIN_OCTETS_LIMIT)
      throw new UserEmailLengthException(
          "Domain-part (after @) exceeds " + DOMAIN_OCTETS_LIMIT + " characters");

    if (validate.length() > EMAIL_OCTETS_LIMIT)
      throw new UserEmailLengthException(
          "Email exceeds character limit of "
              + EMAIL_OCTETS_LIMIT
              + ". Email length: "
              + validate.length());

    email = validate.toLowerCase(Locale.ROOT);
  }
}
