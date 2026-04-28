package com.efpcode.domain.common.model;

import com.efpcode.domain.common.exceptions.InvalidCommonPasswordException;
import java.util.regex.Pattern;

public record PlainPassword(String plainPassword) {
  private static final int MINIMUM_LENGTH = 8;
  private static final Pattern DIGIT = Pattern.compile(".*\\d.*");
  private static final Pattern SYMBOL = Pattern.compile(".*[^a-zA-Z0-9].*");
  private static final Pattern UPPERCASE = Pattern.compile(".*[A-Z].*");

  public PlainPassword {
    if (plainPassword == null || plainPassword.trim().isBlank()) {
      throw new InvalidCommonPasswordException(
          "PlainPassword cannot pass null or blank as argument");
    }

    plainPassword = plainPassword.trim();

    if (plainPassword.length() < MINIMUM_LENGTH) {
      throw new InvalidCommonPasswordException(
          String.format(
              "Password needs to be equal or longer than: %d characters", MINIMUM_LENGTH));
    }

    if (!UPPERCASE.matcher(plainPassword).matches()) {
      throw new InvalidCommonPasswordException(
          "Password must contain at least one uppercase letter");
    }

    if (!DIGIT.matcher(plainPassword).matches()) {
      throw new InvalidCommonPasswordException("Password must contain at least one digits");
    }

    if (!SYMBOL.matcher(plainPassword).matches()) {
      throw new InvalidCommonPasswordException("Password must contain at least one symbol");
    }
  }

  @Override
  public String toString() {
    return "PlainPassword{" + "plainPassword='" + "*".repeat(16) + '\'' + '}';
  }
}
