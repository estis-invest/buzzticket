package com.efpcode.domain.user.model;

import com.efpcode.domain.user.exceptions.InvalidUserEmailException;
import com.efpcode.domain.user.exceptions.UserEmailFormatException;
import com.efpcode.domain.user.exceptions.UserEmailLengthException;
import java.util.regex.Pattern;

public record UserEmail(String email) {
  private static final Pattern FORMAT =
      Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
  private static final String EXAMPLE = "user@domain.io";
  private static final int MAX_CHARACTER_LIMIT = 64;

  public UserEmail {

    if (email == null || email.isBlank())
      throw new InvalidUserEmailException("User email cannot be null or blank");

    if (email.length() > MAX_CHARACTER_LIMIT)
      throw new UserEmailLengthException("Email exceeds character limit of " + MAX_CHARACTER_LIMIT);

    if (!FORMAT.matcher(email).matches())
      throw new UserEmailFormatException(
          "Email has not a valid format expected format: " + EXAMPLE);
  }
}
